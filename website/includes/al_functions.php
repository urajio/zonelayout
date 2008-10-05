<?php

require("class.phpmailer.php");

include_once('al_settings.php');

class LicenseInfo {
    public $owner, $id, $uuid, $license_count, $version, $email_address, $state, $product_name, $cost, $expires_on;
}

class ZoneLayoutLicenseMailer extends PHPMailer {
    var $From     = "sales@atticlabs.com";
    var $SMTPAuth = true;
    var $Username = "sales-al";
    var $Password = SMTP_PASSWORD;
    var $FromName = "Attic Labs LLC";
    var $Host     = "atticlabs.com";
    var $Mailer   = "smtp";
    var $WordWrap = 75;
    
    function error_handler($msg) {
        log_al_error(sprintf("Mail Error: %s", $msg));
    }
}

function db_modify($connection, $query, $query_types = "") {
    
    if ($stmt = $connection->prepare($query)) {
        if (strlen($query_types) > 0) {
            $args = array();
            
            array_push($args, $stmt);
            array_push($args, $query_types);
            
            $param_count = func_num_args();
            
            for ($i = 3; $i < $param_count; $i++) {
                $current_arg = func_get_arg($i);
                array_push($args, $current_arg);
            }
            if (!call_user_func_array('mysqli_stmt_bind_param', $args)) {
                $error_string = $stmt->error;
                $stmt->close();
                throw new Exception(sprintf("Failed to bind parameter to statement: %s", $error_string));
            }
        }
        if (!$stmt->execute()) {
            $error_string = $stmt->error;
            $stmt->close();
            throw new Exception(sprintf("Statement failed to execute: %s", $error_string));
        }
        else {
            $stmt->close();
        }
    }
    else {
        throw new Exception(sprintf("Failed to create prepared statement: %s", $connection->error));
    }
    
}

function db_query($connection, $query, $query_types = "") {
    if ($stmt = $connection->prepare($query)) {
        if (strlen($query_types) > 0) {
            $args = array();
            
            array_push($args, $stmt);
            array_push($args, $query_types);
            
            $param_count = func_num_args();
            
            for ($i = 3; $i < $param_count; $i++) {
                $current_arg = func_get_arg($i);
                array_push($args, $current_arg);
            }
            if (!call_user_func_array('mysqli_stmt_bind_param', $args)) {
                $error_string = $stmt->error;
                $stmt->close();
                throw new Exception(sprintf("Failed to bind parameter to statement: %s", $error_string));
            }
        }
        if (!$stmt->execute()) {
            $error_string = $stmt->error;
            $stmt->close();
            throw new Exception(sprintf("Statement failed to execute: %s", $error_string));
        }
        else {
            $stmt->store_result();
            return $stmt;
        }
    }
    else {
        throw new Exception(sprintf("Failed to create prepared statement: %s", $connection->error));
    }
}

function db_release_query($stmt) {
    $stmt->free_result();
    $stmt->close();
}

function validate_email($email) {
    // Create the syntactical validation regular expression
    $regexp = "^([_a-z0-9-]+)(\.[_a-z0-9-]+)*@([a-z0-9-]+)(\.[a-z0-9-]+)*(\.[a-z]{2,4})$";

    // Presume that the email is invalid
    $valid = 0;

    // Validate the syntax
    if (eregi($regexp, $email)) {
        #list($username,$domaintld) = split("@",$email);
        #// Validate the domain
        #if (getmxrr($domaintld,$mxrecords))

        $valid = 1;
    }
    else {
        $valid = 0;
    }

    return $valid;
}

function send_error_email($msg) {
    $mail = new ZoneLayoutLicenseMailer();
    
    $body = "<p>An error was encountered sending out a license file to a customer.</p>";
    
    $body .= "<p>".$msg."</p>";
    
    $text_body= "An error was encountered sending out a license file to a customer.\n\n";
    
    $text_body .= $msg;
    
    $mail->Body = $body;
    $mail->AltBody = $text_body;
    $mail->AddAddress("bnahas@atticlabs.com");
    $mail->Subject = "Website Error";
    
    if (!$mail->Send()) {
        throw new Exception(sprintf("There was an error sending the email: %s", $mail->ErrorInfo));
    }
    
    return true;
}

function guid() {
    $str = md5(uniqid(rand(),true));
    $guid = substr($str,0,8) . '-' .
               substr($str,8,4) . '-' .
               substr($str,12,4). '-' .
               substr($str,16,4). '-' .
               substr($str,20);
    return $guid;
}

function sign_data($data) {
    if (!$fp = fopen(DATA_FILE_DIRECTORY."/licenseKey.pem", "r")) {
        throw new Exception("Was not able to open license key file.");
    }
    
    $privateKeyData = fread($fp, 8192);
    fclose($fp);
    $privateKey = openssl_get_privatekey($privateKeyData, "");
    
    $signature = "null";
    
    if (!openssl_sign($data, $signature, $privateKey)) {
        throw new Exception(sprintf("Was not able to sign data: %s", openssl_error_string()));
    }
    
    return $signature;
}

function get_data_as_string($data) {
    $sortedKeys = array_keys($data);
    sort($sortedKeys);
    
    $lsv = "<ipnTransaction>\n";
    foreach($sortedKeys as $key) {
        $lsv .= "<value name=\"".$key."\">".$data[$key]."</data>\n";
    }
    $lsv .= "</ipnTransaction>\n";
    
    return $lsv;
}

function save_data_to_file($file,$data) {
    if(file_exists($file) && is_writeable($file)) { $mode="a"; } else { $mode="w"; }
    
    if ($fp=@fopen($file,$mode)) {
     
        fwrite($fp, get_data_as_string($data)); 
        
        fclose($fp); 
    }
    else {
        log_al_error("Could not open data file to save ipn transaction.");
    }
}

function log_al_error($msg) {
    error_log(sprintf("%s\n", $msg), 3, LOG_FILE_DIRECTORY."/errors.log");
}

function db_connect() {
    $mysqli = new mysqli(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_NAME);
            
    if (mysqli_connect_errno()) {
        throw new Exception(sprintf("Connect failed to database: %s", mysqli_connect_error()));
    }
    
    return $mysqli;
}

function db_close($connection) {
    $connection->close();
}

function render_errors() {
    global $errors;
    
    if (sizeof($errors) > 0) {
        echo '<div class="errors"><h3>Errors</h3><ul>';
        foreach ($errors as $error) {
            echo "<li>".$error."</li>";
        }
        echo '</ul></div>';
    }
}

function get_product_file_info($product_name, $version) {
    if (!strcmp($product_name, "ZoneLayout")) {
        $values = array("filename" => "zoneLayout-1.0b4.zip");
    }
    elseif (!strcmp($product_name, "SimpleZoneLayout")) {
        $values = array("filename" => "simpleZoneLayout-1.0.zip");
    }
    elseif (!strcmp($product_name, "ZoneLayout Trial")) {
        $values = array("filename" => "zoneLayout-1.0b1-trial.zip");
    }
    else {
        return null;
    }
    
    $values['file'] = DOWNLOADABLE_FILES_DIRECTORY."/".$values['filename'];
    return $values;
}

function serve_file($product_name, $version) {
    if (! $values = get_product_file_info($product_name, $version)) {
        throw new Exception(sprintf("Unknown product: %s %s", $product_name, $version));
    }
    
    $filename = $values['filename'];
    $file = $values['file'];
    
    if (!$fp = fopen($file, "r")) {
        throw new Exception("Was not able to open file to serve to user.");
    }
    
    header("Content-type: application/octet-stream\n");
    header("Content-disposition: attachment; filename=\"$filename\"\n"); 
    header("Content-transfer-encoding: binary\n");
    header("Content-length: " . filesize($file) . "\n");
    
    fpassthru($fp);
    fclose($fp);
}

function is_valid_license_id($connection, $license_id, $product_name, $version) {
    $retVal = true;
    
    $stmt = db_query($connection, "SELECT product_name, version, state FROM licenses WHERE uuid = ?", "s", $license_id);
    if ($stmt->num_rows() == 0) {
        $retVal = false;
    }
    elseif ($stmt->num_rows() == 1) {
        $stmt->bind_result($db_product_name, $db_version, $db_state);
        $stmt->fetch();
        
        if (! ($db_state === "Valid")) {
            $retVal = false;
        }
        elseif (! ($db_product_name === $product_name)) {
            $retVal = false;
        }
        elseif (! ($db_version == 1)) {
            $retVal = false;
        }
    }
    elseif ($stmt->num_rows() > 1) {
        send_error_email("Multiple entries were found in the database for license ID '".$license_id."'");
        $retVal = false;
    }
    db_release_query($stmt);
    
    return $retVal;
}

function create_license_file($license_info) {
    $license_data_string = $license_info->product_name . $license_info->version . $license_info->owner . $license_info->license_count . $license_info->uuid;
    
    $signature = sign_data($license_data_string);;
    
    $license_file .= "com.atticlabs.license.productName=".$license_info->product_name."\n";
    $license_file .= "com.atticlabs.license.version=".$license_info->version."\n";
    $license_file .= "com.atticlabs.license.owner=".$license_info->owner."\n";
    $license_file .= "com.atticlabs.license.licenseCount=".$license_info->license_count."\n";
    $license_file .= "com.atticlabs.license.uuid=".$license_info->uuid."\n";
    $license_file .= "com.atticlabs.license.signature=".base64_encode($signature)."\n";
    
    return $license_file;
}

function create_trial_license_file($name) {
    $license_info = new LicenseInfo();
    $license_info->owner = $name;
    $license_info->uuid= guid();
    $license_info->license_count = 1;
    $license_info->version = 1;
    $license_info->product_name = "ZoneLayout Trial";

    $expiration_date = mktime(0, 0, 0, date("m")  , date("d") + 22, date("Y"));

    $license_info->expires_on = date("Y-m-d", $expiration_date);

    $license_data_string = $license_info->product_name . $license_info->version . $license_info->owner . $license_info->license_count . $license_info->uuid . $license_info->expires_on;

    $signature = sign_data($license_data_string);;

    $license_file .= "com.atticlabs.license.productName=".$license_info->product_name."\n";
    $license_file .= "com.atticlabs.license.version=".$license_info->version."\n";
    $license_file .= "com.atticlabs.license.owner=".$license_info->owner."\n";
    $license_file .= "com.atticlabs.license.licenseCount=".$license_info->license_count."\n";
    $license_file .= "com.atticlabs.license.uuid=".$license_info->uuid."\n";
    $license_file .= "com.atticlabs.license.expiresOn=".$license_info->expires_on."\n";
    $license_file .= "com.atticlabs.license.signature=".base64_encode($signature)."\n";

    return $license_file;
}

function get_license_info($connection, $license_id) {
    $stmt = db_query($connection, "SELECT id, owner, email_address, license_count, version, state, cost, product_name FROM licenses WHERE uuid = ?", "s", $license_id);
    
    $stmt->bind_result($id, $owner, $email_address, $license_count, $version, $state, $cost, $product_name);
    $stmt->fetch();

    $license_info = new LicenseInfo();
    $license_info->id = $id;
    $license_info->owner = $owner;
    $license_info->uuid= $license_id;
    $license_info->email_address = $email_address;
    $license_info->license_count = $license_count;
    $license_info->version = $version;
    $license_info->state = $state;
    $license_info->cost = $cost;
    $license_info->product_name = $product_name;
    
    db_release_query($stmt);
    
    return $license_info;
}

?>
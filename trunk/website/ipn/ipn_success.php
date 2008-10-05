<?php

//log successful transaction to file or database
include_once('../includes/global_config.inc.php'); 

include_once('../includes/al_functions.php'); 

function save_data_to_database($connection, $data) {

    $insert_query = "INSERT INTO paypal_transactions (txn_id, data, created_at) VALUES ".
        "(?, ?, NOW())";
    
    db_modify($connection, $insert_query, "ss", $data['txn_id'], get_data_as_string($data));

}

function process_ipn($connection, $data) {
    if (!strcmp($data['payment_status'], 'Completed')) {
        $previous_license_id = $data['option_selection1'];
        
        if (1 == $data['item_number']) {
            $product_name = "SimpleZoneLayout";
        }
        elseif (2 == $data['item_number']) {
            $product_name = "ZoneLayout";
        }
    
        if (is_valid_license_id($connection, $previous_license_id, $product_name, 1)) {
            $license_info = update_license($connection, $previous_license_id, $data);
            $license_file = create_license_file($license_info);        
            email_license_file($license_info, $license_file, create_updated_license_email($license_info));
        }
        else {
            $license_info = store_license_to_database($connection, $data);
            $license_file = create_license_file($license_info);        
            email_license_file($license_info, $license_file, create_new_license_email($license_info));
        }
        
        $update_query = "UPDATE licenses SET license_sent_at = NOW() WHERE id = ?";
            
        db_modify($connection, $update_query, "i", $license_info->id);
    }
    else {
        send_error_email("There was a transaction from PayPal who's status was not 'Completed'");
        log_al_error(sprintf("payment status of txn_id %s was: %s", $data['txn_id'], $data['payment_status']));
    }
}

function create_new_license_email($license_info) {
    $body = "<p>Thank you for purchasing a license to use ".$license_info->product_name."!</p>";
    
    $body .= "<p>Your license file is attached to this email.  Please save this file as 'zoneLayout.properties' and ";
    $body .= "make sure it is available in your classpath when using ".$license_info->product_name.". ";
    $body .= "If you have any questions or problems regarding your license, please contact us at sales@atticlabs.com.</p>";
    
    $body .= "<p>You can download ".$license_info->product_name.' at <a href="http://zonelayout.com/download">http://zonelayout.com/download</a>.';
    $body .= "You'll need to use your license ID to access the file.</p>";
    
    $body .= "<p>Your license ID is: ".$license_info->uuid."</p>";
    
    $body .= '<p>For technical support, please use our support forums at <a href="http://zonelayout.com/forums">http://zonelayout.com/forums<a/></p>';
    
    
    $text_body = "Thank you for purchasing a license to use ".$license_info->product_name."!\n";
    
    $text_body .= "\nYour license file is attached to this email.  Please save this file as 'zoneLayout.properties' and ";
    $text_body .= "make sure it is available in your classpath when using ".$license_info->product_name.". ";
    $text_body .= "If you have any questions or problems regarding your license, please contact us at sales@atticlabs.com.\n";
    
    $text_body .= "\nYou can download ".$license_info->product_name.' at http://zonelayout.com/download.';
    $text_body .= "You'll need to use your license ID to access the file.\n";
    
    $text_body .= "\nYour license ID is: ".$license_info->uuid."\n";
    
    $text_body .= '\nFor technical support, please use our support forums at http://zonelayout.com/forums';
    
    return array($body, $text_body);
}

function create_updated_license_email($license_info) {
    $body = "<p>Thank you for purchasing additional licenses to use ".$license_info->product_name."!</p>";
    
    $body .= "<p>Your new license file is attached to this email.  Please save this file as 'zoneLayout.properties' and ";
    $body .= "make sure it is available in your classpath when using ".$license_info->product_name.". ";
    $body .= "If you have any questions or problems regarding your license, please contact us at sales@atticlabs.com.</p>";
    
    $body .= "<p>Your license ID remains the same: ".$license_info->uuid."</p>";
    
    $body .= '<p>For technical support, please use our support forums at <a href="http://zonelayout.com/forums">http://zonelayout.com/forums<a/></p>';
    
    
    $text_body = "Thank you for purchasing additional licenses to use ".$license_info->product_name."!\n";
    
    $text_body .= "\nYour new license file is attached to this email.  Please save this file as 'zoneLayout.properties' and ";
    $text_body .= "make sure it is available in your classpath when using ".$license_info->product_name.". ";
    $text_body .= "If you have any questions or problems regarding your license, please contact us at sales@atticlabs.com.\n";
    
    $text_body .= "\nYour license ID remains the same: ".$license_info->uuid."\n";
    
    $text_body .= '\nFor technical support, please use our support forums at http://zonelayout.com/forums';
    
    return array($body, $text_body);
}

function email_license_file($license_info, $license_file, $bodies) {
    $mail = new ZoneLayoutLicenseMailer();
    
    $mail->Subject = $license_info->product_name." License";
    
    $mail->Body = $bodies[0];
    $mail->AltBody = $bodies[1];
    $mail->AddAddress($license_info->email_address);
    #$mail->AddAddress("briannahas@yahoo.com");
    $mail->AddStringAttachment($license_file, "zoneLayout.properties", "base64", "text/text");
    
    if (!$mail->Send()) {
        throw new Exception(sprintf("There was an error sending the email: %s", $mail->ErrorInfo));
    }
    return true;
}

function check_cost($data) {
    if (1 == $data['item_number']) {
        $product_name = "SimpleZoneLayout";
    }
    elseif (2 == $data['item_number']) {
        $product_name = "ZoneLayout";
    }
    $quantity = $data['quantity'];
    $cost = $data['mc_gross'];
    $txn_id = $data['txn_id'];
    
    if ($product_name === "SimpleZoneLayout") {
        if ($quantity * 99 > $cost) {
            throw new Exception(sprintf("Someone attempted to by SimpleZoneLayout licenses for less than list.  PayPal txn_id: %s", $txn_id));
        }
    }
    else {
        if ($quantity * 39 > $cost) {
            throw new Exception(sprintf("Someone attempted to by ZoneLayout licenses for less than list.  PayPal txn_id: %s", $txn_id));
        }
    }
}

function update_license($connection, $license_id, $data) {
    check_cost($data);
    
    $email_address = trim($data['payer_email']);
    $license_count = $data['quantity'];
    $cost = $data['mc_gross'];
    
    $stmt = db_query($connection, "SELECT license_count, cost FROM licenses WHERE uuid = ?", "s", $license_id);
    
    $stmt->bind_result($current_license_count, $current_cost);
    $stmt->fetch();
    
    $license_count = $license_count + $current_license_count;
    $cost = $cost + $current_cost;
    
    db_release_query($stmt);
    
    $update_query = "UPDATE licenses SET license_count = ?, cost = ?, modified_at = NOW() WHERE uuid = ?";
    
    db_modify($connection, $update_query, "ids", $license_count, $cost, $license_id);
    
    $license_info = get_license_info($connection, $license_id);
    
    return $license_info;
}

function store_license_to_database($connection, $data) {
    check_cost($data);
    
    $owner = trim($data['option_selection1']);
    if (strlen($owner) == 0) {
        send_error_email("There was a transaction received from PayPal with no owner. Using Names instead.");
        $owner = trim($data['first_name'])." ".trim($data['last_name']);            
    }
    $uuid = guid();
    $email_address = trim($data['payer_email']);
    if (1 == $data['item_number']) {
        $product_name = "SimpleZoneLayout";
    }
    elseif (2 == $data['item_number']) {
        $product_name = "ZoneLayout";
    }
    $license_count = $data['quantity'];
    $version = 1;
    $state = "Valid";
    $cost = $data['mc_gross'];
    $insert_query = "INSERT INTO licenses (uuid, owner, email_address, ".
    "product_name, version, license_count, cost, state, created_at, modified_at) VALUES ".
    "(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
    
    db_modify($connection, $insert_query, "ssssiids", $uuid, $owner, 
        $email_address, $product_name, $version, $license_count, $cost, $state);
    
    $license_info = get_license_info($connection, $uuid);
    
    return $license_info;
}

try {
    #TODO need to insert test exclusion
    #TODO need to ignore other types of payments
    
    save_data_to_file(LOG_FILE_DIRECTORY."/ipn_success.txt",$_POST);
    
    $connection = db_connect();
    
    save_data_to_database($connection, $_POST);
    process_ipn($connection, $_POST);
    
    db_close($connection);
}
catch (Exception $exception) {
    log_al_error($exception->getMessage());
    send_error_email($exception->getMessage());
}

?> 
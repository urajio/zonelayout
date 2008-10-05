<?php
/*
 * ipn_error.php
 *
 * PHP Toolkit for PayPal v0.50
 * http://www.paypal.com/pdn
 *
 * Copyright (c) 2004 PayPal Inc
 *
 * Released under Common Public License 1.0
 * http://opensource.org/licenses/cpl.php
 *
*/

//log error to file or database

include_once('../includes/global_config.inc.php'); 

include_once('../includes/al_functions.php'); 

save_data_to_file(LOG_FILE_DIRECTORY."/ipn_error.txt",$_POST);

?> 
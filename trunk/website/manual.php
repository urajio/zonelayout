<?php

function get_page_title() {
    return "ZoneLayout.com Manual";
}

function render_page() {
?>
@MANUAL_CONTENT@
<?php
}

include_once('includes/page.php');

?>

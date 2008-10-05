<?php

include_once('header.php');
include_once('menubar.php');

?>
<div id="content">
<?php

try {
    render_page();
}
catch (Exception $exception) {
    log_al_error($exception->getMessage());
    send_error_email($exception->getMessage());
?>
<p>An error was encountered processing your request.  Technical support has been notified and we hope
to correct the problem soon.  We apologize for this inconvenience.</p>
<?php
}

?>
</div>
<?php

include_once('footer.php');

?>
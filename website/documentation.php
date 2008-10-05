<?php

include_once('includes/al_functions.php');

function get_page_title() {
    return "ZoneLayout.com Documentation";
}

function render_page() {
?>
<h2><a href="manual.php">Manual</a></h2>

<p>The ZoneLayout manual has a lot of detailed information on how to use ZoneLayout.  If you're new to ZoneLayout
and want to learn how it works, that's the place to start.</p>

<h2><a href="images/cheatsheet.png">Cheatsheet</a></h2>

<p>The cheatsheet provides almost all you need to know about ZoneLayout on one handy little page.  Print this
out and then get to work.  There's a higher resolution pdf of this included in your ZoneLayout download.</p>
<?php
}

include_once('includes/page.php');

?>

<?php

function get_page_title() {
    return "ZoneLayout.com Demo";
}

function render_page() {
?>
<p>
    You need the Java plugin version 1.4.2 or later to run this demo (it's an applet).
</p>

<p>
    To use the demo, enter that layout code in the TextArea labeled "Layout Code" and then push the "Compile" button.
    Your layout code will then be compiled and the results will be rendered in the "Results" panel.  Since there's no
    component binding involved here, the applet will place JButtons in all of your zones for you.  If there are any
    errors they will appear in the "Errors" TextArea.
</p>

<p>
    For example, if you enter "abcd" in the "Layout Code" TextArea and then push "Compile", the demo will compile your
    layout into four Zones ('a', 'b', 'c', and 'd') and then render for JButtons in the "Results" panel.
</p>

<p>
    The <a href="images/cheatsheet.png" target="_blank">cheatsheet</a> (pop up) will probably be of some use to you
    here.  Or if you're completely new to the concept behind ZoneLayout, you can start with the
    <a href="manual.php">manual</a>.
</p>

<applet code="com.atticlabs.zonelayout.swing.examples.DemoApplet" width="700" height="500" archive="zoneLayoutDemo.jar">
</applet>
<?php
}

include_once('includes/page.php');

?>
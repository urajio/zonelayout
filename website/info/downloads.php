<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>ZoneLayout.com Trial Download Info</title>

<link rel="stylesheet" href="../style.css" type="text/css" media="screen" />

<!--[if gte IE 5.5000]>
<script language="JavaScript">
function correctPNG() // correctly handle PNG transparency in Win IE 5.5 or higher.
   {
   for(var i=0; i<document.images.length; i++)
      {
	  var img = document.images[i]
	  var imgName = img.src.toUpperCase()
	  if (imgName.substring(imgName.length-3, imgName.length) == "PNG")
	     {
		 var imgID = (img.id) ? "id='" + img.id + "' " : ""
		 var imgClass = (img.className) ? "class='" + img.className + "' " : ""
		 var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "
		 var imgStyle = "display:inline-block;" + img.style.cssText
		 if (img.align == "left") imgStyle = "float:left;" + imgStyle
		 if (img.align == "right") imgStyle = "float:right;" + imgStyle
		 if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle
		 var strNewHTML = "<span " + imgID + imgClass + imgTitle
		 + " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"
	     + "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
		 + "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>"
		 img.outerHTML = strNewHTML
		 i = i-1
	     }
      }
   }
window.attachEvent("onload", correctPNG);
</script>
<![endif]-->

</head>
<body>
<div id="content">
<?php

include_once('../includes/al_functions.php');

if (! ($_POST['password'] === 'sim0nsays')) {
?>
<form action="downloads.php" method="post">
    <p>Password: <input type="password" name="password"/></p>
    <p><input type="submit" name="submit" value="submit"/></p>
</form>
<?php
}
else {
?>
<table width="100%" class="simpleTable">
<?php
    $connection = db_connect();
    $stmt = db_query($connection, "SELECT id, ip_address, downloaded_at FROM downloads");

    $stmt->bind_result($db_id, $db_ip_address, $db_downloaded_at);

    while ($stmt->fetch()) {
?><tr><td><?php echo $db_id ?></td><td><?php echo $db_ip_address ?></td><td><?php echo $db_downloaded_at ?></td></tr><?php
    }

    db_release_query($stmt);
    db_close($connection);
?>
</table>
<?php
}

?>
</div>
</body>
</html>

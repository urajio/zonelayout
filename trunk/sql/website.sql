-- MySQL dump 10.9
--
-- Host: localhost    Database: licenses
-- ------------------------------------------------------
-- Server version	4.1.16-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `downloads`
--

DROP TABLE IF EXISTS `downloads`;
CREATE TABLE `downloads` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `ip_address` varchar(15) NOT NULL default '',
  `license_id` int(10) unsigned NOT NULL default '0',
  `downloaded_at` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `licenses`
--

DROP TABLE IF EXISTS `licenses`;
CREATE TABLE `licenses` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `uuid` varchar(36) NOT NULL default '',
  `version` int(10) unsigned NOT NULL default '0',
  `product_name` varchar(127) NOT NULL default '',
  `owner` varchar(255) NOT NULL default '',
  `license_count` int(10) unsigned NOT NULL default '0',
  `created_at` datetime NOT NULL default '0000-00-00 00:00:00',
  `modified_at` datetime NOT NULL default '0000-00-00 00:00:00',
  `email_address` varchar(127) NOT NULL default '',
  `license_sent_at` datetime default NULL,
  `cost` decimal(10,2) NOT NULL default '0.00',
  `state` set('Valid','Refunded','Pirated') NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `paypal_transactions`
--

DROP TABLE IF EXISTS `paypal_transactions`;
CREATE TABLE `paypal_transactions` (
  `txn_id` varchar(19) NOT NULL default '',
  `data` text NOT NULL,
  `created_at` datetime NOT NULL default '0000-00-00 00:00:00',
  `id` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `trial_downloads`
--

DROP TABLE IF EXISTS `trial_downloads`;
CREATE TABLE `trial_downloads` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `ip_address` varchar(15) NOT NULL default '',
  `downloaded_at` datetime NOT NULL default '0000-00-00 00:00:00',
  `email_address` varchar(255) NOT NULL default '',
  `name` varchar(127) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


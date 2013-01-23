--
-- Table structure for table `cast_casters`
--

DROP TABLE IF EXISTS `cast_casters`;
CREATE TABLE `cast_casters` (
  `cast_id` bigint(20) NOT NULL,
  `caster_id` bigint(20) NOT NULL,
  KEY `FKFE0444C7E7D49C94` (`caster_id`),
  KEY `FKFE0444C7C6F9A815` (`cast_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cast_casters`
--

--
-- Table structure for table `casts`
--

DROP TABLE IF EXISTS `casts`;
CREATE TABLE `casts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `broadcast_date` datetime NOT NULL,
  `description` longtext NOT NULL,
  `language` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `video_url` varchar(255) NOT NULL,
  `viewer_count` int(11) NOT NULL,
  `related_game` bigint(20) NOT NULL,
  `casts` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`),
  UNIQUE KEY `video_url` (`video_url`),
  KEY `FK5A0ED141BE44453` (`casts`),
  KEY `FK5A0ED14AF0F0A33` (`related_game`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `casts`
--


--
-- Table structure for table `configurable_slots`
--

DROP TABLE IF EXISTS `configurable_slots`;
CREATE TABLE `configurable_slots` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext NOT NULL,
  `link` varchar(255) NOT NULL,
  `picture` varchar(255) NOT NULL,
  `title` varchar(100) NOT NULL,
  `position` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `configurable_slots`
--


--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext NOT NULL,
  `end_date` datetime NOT NULL,
  `start_date` datetime NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `events`
--


--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
CREATE TABLE `games` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext NOT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `games`
--

INSERT INTO `games` VALUES (1,'starcraft 2','starcraft2');

--
-- Table structure for table `gondola_slides`
--

DROP TABLE IF EXISTS `gondola_slides`;
CREATE TABLE `gondola_slides` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `description` longtext NOT NULL,
  `language` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `picture` varchar(255) NOT NULL,
  `prize` varchar(50) NOT NULL,
  `tag_line` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `gondola_slides`
--


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `nickname` varchar(100) NOT NULL,
  `password` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

--
-- Table structure for table `video_providers`
--

DROP TABLE IF EXISTS `video_providers`;
CREATE TABLE `video_providers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pattern` varchar(255) NOT NULL,
  `template` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pattern` (`pattern`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `video_providers`
--

INSERT INTO `video_providers` VALUES (1,'^(?:(?:https?)://)?(?:www.)?youtube.com/watch?(?:.*)v=([A-Za-z0-9._%-]{11}).*','<iframe width="425" height="349" src="https://www.youtube.com/embed/{ID}" frameborder="0" allowfullscreen></iframe>');
INSERT INTO `video_providers` VALUES (2,'^(?:(?:https?)://)?(?:www.)?justin.tv/([a-zA-Z0-9]+).*','<object type="application/x-shockwave-flash" id="live_embed_player_flash" data="http://www.justin.tv/widgets/live_embed_player.swf?channel={ID}" bgcolor="#000000" height="300" width="400">\n <param name="allowFullScreen" value="true">\n <param name="allowScriptAccess" value="always">\n <param name="allowNetworking" value="all">\n <param name="movie" value="http://www.justin.tv/widgets/live_embed_player.swf">\n <param name="flashvars" value="channel={ID}&amp;auto_play=false&amp;start_volume=25">\n</object>');
INSERT INTO `video_providers` VALUES (3,'^(?:(?:https?)://)?(?:www.)?dailymotion.com/video/([a-zA-Z0-9]+)_.*','<iframe frameborder="0" width="480" height="270" src="http://www.dailymotion.com/embed/video/{ID}"></iframe>');
INSERT INTO `video_providers` VALUES (4,'^(?:(?:https?)://)?(?:www.)?livestream.com/([a-zA-Z0-9]+)','<iframe width="560" height="340" src="http://cdn.livestream.com/embed/{ID}?layout=4&amp;autoplay=false" style="border:0;outline:0" frameborder="0" scrolling="no"></iframe>');
INSERT INTO `video_providers` VALUES (5,'^(?:(?:https?)://)?(?:www\.)?own3d.tv/live/([0-9]+).*','<iframe height="360" width="640" frameborder="0" src="http://www.own3d.tv/liveembed/{ID}"></iframe>');
INSERT INTO `video_providers` VALUES (6,'^(?:(?:https?)://)?(?:www\.)?regame.tv/(?:live|vod)/([0-9]+).*','<iframe src="http://www.regame.tv/playflash.php?vid={ID}&remote=true&w=630&h=400" width="650" height="420" frameborder=0></iframe>');
INSERT INTO `video_providers` VALUES (7,'^(?:(?:https?)://)?(?:www\.)?twitch.tv/([a-zA-Z0-9]+).*','<object type="application/x-shockwave-flash" height="300" width="400" id="live_embed_player_flash" data="http://www.twitch.tv/widgets/live_embed_player.swf?channel={ID}" bgcolor="#000000"><param name="allowFullScreen" value="true" /><param name="allowScriptAccess" value="always" /><param name="allowNetworking" value="all" /><param name="movie" value="http://www.twitch.tv/widgets/live_embed_player.swf" /><param name="flashvars" value="hostname=www.twitch.tv&channel={ID}&auto_play=false&start_volume=25" /></object>');
INSERT INTO `video_providers` VALUES (8,'^(?:(?:https?)://)?(?:www\.)?ustream.tv/channel/([0-9]+).*','<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="480" height="296" id="utv100404">
 <param name="flashvars" value="autoplay=false&amp;brand=embed&amp;cid={ID}&amp;v3=1"/>\n
 <param name="allowfullscreen" value="true"/><param name="allowscriptaccess" value="always"/>\n
 <param name="movie" value="http://www.ustream.tv/flash/viewer.swf"/>\n
 <embed flashvars="autoplay=false&amp;brand=embed&amp;cid={ID}&amp;v3=1" width="480" height="296" allowfullscreen="true" allowscriptaccess="always" src="http://www.ustream.tv/flash/viewer.swf" type="application/x-shockwave-flash" />\n
</object>');

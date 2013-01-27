/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	config.language = 'en',
	// config.uiColor = '#AADC6E';
	config.toolbar = [ ['FontSize'], ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About'], [ 'Image', 'Table', 'HorizontalRule' ] ];
	config.autoParagraph = false;
};

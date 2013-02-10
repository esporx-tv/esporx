// loader configuration
require.config({
    map : {
        '*' : {
            'text' : 'ext/text',
            'css' : '../css',
            'tpl' : '../tpl'
        }
    },
    paths : {
        jqueryui: 'ext/jquery-ui-custom',
        underscore: 'ext/underscore'
    }
});
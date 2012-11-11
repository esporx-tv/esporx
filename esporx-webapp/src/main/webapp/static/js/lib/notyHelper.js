define (["jquery", "ext/noty/jquery.noty", "ext/noty/layouts/bottomRight", "ext/noty/layouts/center", "ext/noty/themes/default"], function($){
    $.noty.defaults = {
      layout: 'bottomRight',
      theme: 'default',
      type: 'alert',
      text: '',
      dismissQueue: true, // If you want to use queue feature set this true
      template: '<div class="noty_message"><span class="noty_text"></span><div class="noty_close"></div></div>',
      animation: {
        open: {height: 'toggle'},
        close: {height: 'toggle'},
        easing: 'swing',
        speed: 500 // opening & closing animation speed
      },
      timeout: false, // delay for closing event. Set false for sticky notifications
      force: false, // adds notification to the beginning of queue when set to true
      modal: false,
      closeWith: ['click'], // ['click', 'button', 'hover']
      callback: {
        onShow: function() {},
        afterShow: function() {},
        onClose: function() {},
        afterClose: function() {}
      },
      buttons: false // an array of buttons
    };
    return {};
});
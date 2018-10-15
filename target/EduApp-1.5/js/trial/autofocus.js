
angular
.module('ec.autofocus', [])
.directive('autofocus', ['$document', function($document) {
  return {
    link: function($scope, $element, attrs) {
      setTimeout(function() {
        $element[0].focus();
      }, 100);
    }
  };
}])
;

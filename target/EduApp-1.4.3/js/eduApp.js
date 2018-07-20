
var eduApp = angular.module('eduApp', [ 'ui.router', 'ngFileUpload', 'ngCookies',
		'angularUtils.directives.dirPagination', 'selectize','chart.js' ]);

eduApp.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

	$urlRouterProvider.otherwise('/');

/**
 * *************************** PUBLIC
 * Start**************************************
 */
	$stateProvider.state('scholarshipApp', {
		url : '/',
		views : {
			'sideBar' : {
				templateUrl : '/templates/public/publicSideBar.html',
				controller : 'PublicSideBarCtrl'
			},
			'mainPanel' : {
				templateUrl : '/templates/public/eduAppInstruction.html',
				controller: 'PublicAppInstructionCtrl'
			}
		}
	}).state('scholarshipApp.overview', {
		url : 'overview',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/public/eduAssistanceOverview.html'
			}
		}	
	}).state('scholarshipApp.instruction', {
		url : 'instruction',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/public/eduAppInstruction.html',
				controller: 'PublicAppInstructionCtrl'
			}
		}
	}).state('scholarshipApp.application', {
		url : 'application/:oldStudent?studentId?birthDate',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/public/eduApplication.html',
				controller : 'EduApplicationCtrl'
			}
		}
	}).state('scholarshipApp.applicationConfirmation', {
		url : 'applicationConfirmation',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/public/eduAppConfirm.html',
				controller : 'EduAppConfirmationCtrl'
			}
		}
	}).state('scholarshipApp.status', {
		url : 'status/:confirmationNmbr?studentId?birthDate',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/public/eduAppStatus.html',
				controller : 'EduAppStatusCtrl'
			}
		}
	}).state('forgot', {
		url : '/forgot',
		views : {
			'sideBar' : {
				templateUrl : '/templates/public/publicSideBar.html'
			},
			'mainPanel' : {
				templateUrl : '/templates/public/forgot.html',
				controller : 'forgotCtrl as controller'
			}
		}
	}).state('reset', {
		url : '/reset/:token',
		views : {
			'sideBar' : {
				templateUrl : '/templates/public/publicSideBar.html'
			},
			'mainPanel' : {
				templateUrl : '/templates/public/reset.html',
				controller : 'resetCtrl as controller'
			}
		}
	}).state('login', {
		url : '/login',
		views : {
			'sideBar' : {
				templateUrl : '/templates/public/publicSideBar.html'
			},
			'mainPanel' : {
				templateUrl : '/templates/public/login.html',
				controller : 'navCtrl as controller'
			}
		}
	})
/** *************************** PUBLIC End************************************** */
	
/** *************************** Committee Start********************************* */	
	.state('appProcessing', {
		url : '/appProcessing',
		views : {
			'sideBar' : {
				templateUrl : '/templates/committee/eduAppProcessSideBar.html'
			},
			'mainPanel' : {
				templateUrl : '/templates/committee/eduAppDashBoard.html',
				controller : 'EduAppDashBoardCtrl'
			}
		}
	}).state('appProcessing.dashBoard', {
		url : 'dashBoard',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/eduAppDashBoard.html',
				controller : 'EduAppDashBoardCtrl'
			}
		}
	}).state('appProcessing.myAssignment', {
		url : 'myAssignment',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/eduAppMyAssignment.html',
				controller : 'EduAppMyAssignmentCtrl'
			}
		}
	}).state('appProcessing.processApp', {
		url : 'processApp/:applId',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/eduAppProcessApp.html',
				controller : 'EduAppProcessAppCtrl'
			}
		}	
	}).state('appProcessing.newApplications', {
		url : 'newApplications',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/eduAppNewApplications.html',
				controller : 'EduAppNewApplicationsCtrl'
			}
		}
	}).state('appProcessing.changePasswd', {
		url : 'changePasswd',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/changePasswd.html',
				controller : 'changePasswdCtrl'
			}
		}
	}).state('appProcessing.reports', {
		url : 'reports',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/reports.html',
				controller : 'reportsCtrl'
			}
		}	
	}).state('appProcessing.search', {
		url : 'search',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/eduAppSearch.html',
				controller : 'EduAppSearchCtrl'
			}
		}	
/** *************************** Committee End********************************** */		

/** *************************** Admin Start *********************************** */	
	}).state('appProcessing.ranking', {
		url : 'ranking',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/ranking.html',
				controller : 'rankingCtrl'
			}
		}	
	}).state('appProcessing.applicationsAwdAdmin', {
		url : 'applicationsAwdAdmin',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/applicationsAwdAdmin.html',
				controller : 'ApplicationsAwdAdminCtrl'
			}
		}
	}).state('appProcessing.appConfig', {
		url : 'appConfig',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/appConfig.html',
				controller : 'appConfigCtrl'
			}
		}	
	}).state('appProcessing.userMgmt', {
		url : 'userMgmt',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/userMgmt.html',
				controller : 'userMgmtCtrl'
			}
		}	
	}).state('appProcessing.userEdit', {
		url : 'userEdit/:userId',
		views : {
			'mainPanel@' : {
				templateUrl : '/templates/committee/userEdit.html',
				controller : 'editUserCtrl'
			}
		}	
	})
/** *************************** Admin End ************************************ */		
	
	.state('notFound', {
        url: '/errors/404',
        template: '<h1>Not Found</h1>' 
    })
        
     .state('accessDenied', {
        url: '/errors/403',
        views : {
			'mainPanel' : {
				template: '<h1>Access Denied</h1>' 
			}
		}
                 
    });
    
	// Intercept 401s and 403s and redirect you to login
	$httpProvider.interceptors.push(['$q', '$location', '$injector', '$rootScope', function($q, $location, $injector, $rootScope) {
	  return {
	    'responseError': function(response) {
	      if(response.status === 403) {
	    	  console.log('/errors/403');
	        // $location.path('/error/403');
			// $state.go('accessDenied', {}, {location: false});
	        return $q.reject(response);
	      }
	      else if (response.status === 440) {
	    	  console.log('/errors/440');
		        // $location.path('/error/403');
				// $state.go('accessDenied', {}, {location: false});
	    	  	window.alert("Session Expired. Please try again!");
	    	  	$injector.get('$state').go('scholarshipApp');
		        return $q.reject(response);
		      }
	      else if (response.status === 401) {
	    	  console.log('/errors/401');
		        // $location.path('/error/403');
				// $state.go('accessDenied', {}, {location: false});
	    	  	window.alert("Session Expired or Invalid Login. Please login!");
	    	  	delete $rootScope.authenticated;
	    	  	delete window.sessionStorage.authenticated;
	    	  	console.log($injector.get('$rootScope').authenticated);
	    	  	$rootScope.name = undefined;
	    	  	$rootScope.userRole = undefined;
	    	  	$injector.get('$state').go('login', {}, {location: false});
		        return $q.reject(response);
		      }
	      else {
	    	  console.log(response.status);
	        return $q.reject(response);
	      }
	    }
	  };
	}]);	
	
})
.run(function ($rootScope, $state) {
    $rootScope.$on("$stateChangeError", function(event, toState, toParams, fromState, fromParams){
    	console.log('statechange error');
    	console.log(fromState +":" + toState );
    	event.preventDefault();
    	$state.go('accessDenied', {}, {location: false});
        /*
		 * if (toState.authenticate && !Auth.isLoggedIn()){ // User isn’t
		 * authenticated $state.transitionTo("login"); event.preventDefault(); }
		 */
    });    
});

eduApp.controller('navCtrl',  function( $rootScope,  $http, $location, $state, $window) {

	var self = this
	$rootScope.name = $window.sessionStorage.userName;
	$rootScope.userRole = $window.sessionStorage.userRole;
    $rootScope.authenticated = $window.sessionStorage.authenticated;
    
	var authenticate = function(credentials, callback) {
		var headers = credentials ? {authorization : "Basic "
		        + btoa(credentials.username + ":" + credentials.password)
		} : {};
	
	    $http.get('user', {headers : headers}).then(function(response) {
	      if (response.data.userName) {
	    	$rootScope.name = response.data.userName;
	    	$rootScope.userRole = response.data.userRole;
	    	$rootScope.authenticated = true;
	        
	    	//store in windows session scope, so the values are available
	    	// after page refresh
	        $window.sessionStorage.userName = response.data.userName;
	        $window.sessionStorage.userRole = response.data.userRole;
	        $window.sessionStorage.authenticated = true;
	      } else {
	    	delete $rootScope.authenticated;
	        delete $window.sessionStorage.authenticated;
	      }
	      callback && callback();
	    }, function() {
	    	delete $rootScope.authenticated;
	        delete $window.sessionStorage.authenticated;
	      callback && callback();
	    });
	
	  }

	  //authenticate();
		self.credentials = {};
		self.login = function() {
		  document.body.style.cursor='wait';
	      authenticate(self.credentials, function() {
	        if ($rootScope.authenticated) {
	        	$state.go('appProcessing');
	          // $location.path("/");
	          self.error = false;
	        } else {
	        	$state.go('login');
	          // $location.path("/login");
	          self.error = true;
	        }
	      });
	      document.body.style.cursor='default';
	  };
	  
	  self.logout = function() {
		  $http.post('logout', {}).finally(function() {
			delete $rootScope.authenticated;
		    delete $rootScope.name;
		    delete $rootScope.userRole;
		    
		    delete $window.sessionStorage.authenticated;
	        delete $window.sessionStorage.userName;
	        delete $window.sessionStorage.userRole;		    
		    $state.go('scholarshipApp');
		    // $location.path("/");
		  });
		}	  
});

//Directive for custom form field validation
eduApp.directive('customValidationError', function () {
	  return {
	    require: 'ngModel',
	    link: function (scope, elm, attrs, ctl) {
	      scope.$watch(attrs['customValidationError'], function (errorMsg) {
	        elm[0].setCustomValidity(errorMsg);
	        ctl.$setValidity('customValidationError', errorMsg ? false : true);
	      });
	    }
	  };
	});




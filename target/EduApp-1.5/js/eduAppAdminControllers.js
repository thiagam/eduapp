eduApp.controller('appConfigCtrl',  function( $scope, $http, $state, $window) {
   			document.body.style.cursor='wait';
   			
   			$http.get('/public/getEduAppConfig')
   			.then(function(response) {
   				$scope.appConfig = response.data;
   			})
			.finally(function () {
				document.body.style.cursor='default';
			}); 
   			
   			// function to submit changes made to AppConfig
   			$scope.submitEduappConfig = function() {
   				document.body.style.cursor='wait';  
   				$http.post('/saveAppConfig', $scope.appConfig)
   				.then(function(response) {
   					$scope.saveSuccess=response.data;
   					if ($scope.saveSuccess) {
   						$window.alert('Data Saved Successfully');
   					} else {
   	   					$window.alert('Save failed. Application year can not be set to older years.');
   					}

   				})
   				.finally(function () {
   					document.body.style.cursor='default';
   				});
   			};   			

});

eduApp.controller('rankingCtrl',  function( $scope, $http, $state) {
	$scope.clearRanking = function() {
		document.body.style.cursor='wait';
		$http.post('/clearRanking')
		.then(function(response) {
			window.alert("clear Ranking Success");
		}, function(error){
			window.alert("clear Ranking failed");
			if(error.status === 401 || error.status === 403){
				$state.go('accessDenied', {}, {location: false});
			}
		}).finally(function() {
		    document.body.style.cursor='default';
		});
	}
	
	$scope.rankApp = function() {
		document.body.style.cursor='wait';
		$http.post('/rankApplication')
		.then(function(response) {
			window.alert("Ranking Completed Successfully");
		}, function(error){
			window.alert("Ranking failed");
			if(error.status === 401 || error.status === 403){
				$state.go('accessDenied', {}, {location: false});
			}
		}).finally(function() {
		    document.body.style.cursor='default';
		});		
	}	
	
	$scope.setDefaultAwardAmount = function() {
		document.body.style.cursor='wait';
		$http.post('/setDefaultAwardAmount')
		.then(function(response) {
			window.alert("Default award amount set successfully");
		}, function(error){
			window.alert("Settind default award amount failed");
			if(error.status === 401 || error.status === 403){
				$state.go('accessDenied', {}, {location: false});
			}
		}).finally(function() {
		    document.body.style.cursor='default';
		});
	}	
	
	$scope.setStatusToAwardedForApprovedWithBankDetails = function() {
		document.body.style.cursor='wait';
		$http.post('/setStatusToAwardedForApprovedWithBankDetails')
		.then(function(response) {
			window.alert("Applications ready for Award marked Awarded successfully");
		}, function(error){
			window.alert("Action failed");
			if(error.status === 401 || error.status === 403){
				$state.go('accessDenied', {}, {location: false});
			}
		}).finally(function() {
		    document.body.style.cursor='default';
		});
	}	
});

eduApp.controller('editUserCtrl',  function( $scope, $http, $state, $window, $stateParams) {
	
    var userId = $stateParams.userId; 
    
	document.body.style.cursor='wait';
	
	$http.get('/getUser?userId='+ userId)
	.then(function(response) {
		$scope.user = response.data;
	})
	.finally(function () {
		document.body.style.cursor='default';
	}); 
	
	// function to submit changes made to user
	$scope.saveUser = function() {
		document.body.style.cursor='wait';  
		$http.post('/saveUser', $scope.user)
		.then(function(response) {
			result = response.data;
			if (result == 0) {
				$window.alert('Data Saved Successfully');
			} else if (result == -1) {
				$window.alert('Data Saved failed. "User Name" already exists. Please use different "User Name".');
			}
		})
		.finally(function () {
			document.body.style.cursor='default';
		});
	};  	
});	

eduApp.controller('userMgmtCtrl',  function( $scope, $http, $state, $window) {
		document.body.style.cursor='wait';
		
		$http.get('/getUserMgmtList')
		.then(function(response) {
			$scope.users = response.data;
		})
		.finally(function () {
			document.body.style.cursor='default';
		}); 
		
		/*
		// initial sorting by user name
		$scope.sort = {
			column : 'userName',
			descending : false
		};
		*/
		// Function to sort table
		$scope.changeSorting = function(column) {
			var sort = $scope.sort;
			if (sort.column == column) {
				sort.descending = !sort.descending;
			} else {
				sort.column = column;
				sort.descending = false;
			}
		};		
		
});

eduApp.controller('ApplicationsAwdAdminCtrl', [
	'$scope',
	'$http',
	'$window',
	'$state',
	function($scope, $http, $window, $state) {
		document.body.style.cursor='wait';
	
		// load all
		// applications in status 'ReviewComplete' or 'Approved'
		$http.get('/loadApplicationsForAwdAdmin')
		.then(function(response) {
			$scope.appls = response.data;
			var arrayLength = $scope.appls.length;
			for (var i = 0; i < arrayLength; i++) {
				$scope.appls[i].crTS = new Date($scope.appls[i].crTS);
			}   				
		}, function(error){
			if(error.status === 401 || error.status === 403){
				$state.go('accessDenied', {}, {location: false});
			}
		})
		.finally(function () {
			document.body.style.cursor='default';
		}); 			
		
		// initial sorting by application
		// submission(received) time
		$scope.sort = {
			column : 'eduappProcessDetail.rank',
			descending : false
		};
		
		$scope.changeSorting = function(column) {
			var sort = $scope.sort;
			if (sort.column == column) {
				sort.descending = !sort.descending;
			} else {
				sort.column = column;
				sort.descending = false;
			}
		};  
		
		$scope.submitReviewerAssignments = function() {
			document.body.style.cursor='wait';
			$http.post('/submitEduApplProcessDetails', $scope.appls)
			.then(function(response) {
				$window.alert('Data Saved Successfully');
			})
			.finally(function () {
				document.body.style.cursor='default';
			}); 
		};			
	} 
]);


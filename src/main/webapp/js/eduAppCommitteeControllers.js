eduApp.controller('EduAppDashBoardCtrl', [
	'$scope',
	'$http',
	function($scope, $http) {
		document.body.style.cursor='wait';
		$http.get('/public/getEduAppConfig')
		.then(function(response) {
			$scope.currentYear = response.data.appYear;
			$scope.applicationYear = $scope.currentYear;
			$scope.selectedYear = $scope.currentYear;
		});		
		
		//Overall Progress Data for chart///////////////
		$scope.rptOverallProgresslabels = ['Pending', 'Completed'];
		$scope.rptOverallProgressData= [];
		
		$http.get('/getOverAllProgress')
		.then(function(response) {
			$scope.rptOverallProgressData[0] = response.data[0].assignedCount
			$scope.rptOverallProgressData[1] = response.data[0].completedCount
			
		})
		/////////////////////////////////////////////////////		
		
		//Reviewer progress Data for chart///////////////
		$scope.labels = [];
		$scope.series = ['Assigned', 'Completed'];
		$scope.reportData = [0,1];
		$scope.reportData[0]=[];
		$scope.reportData[1]=[];
					
		$http.get('/getReviewerProgress')
		.then(function(response) {
			data = response.data;
			for (var i = 0; i < data.length; i++) {
				$scope.labels[i] = data[i].reviewer;
				$scope.reportData[0][i] = data[i].assignedCount;
				$scope.reportData[1][i] = data[i].completedCount;
			}
		})
		/////////////////////////////////////////////////////
		
		
		//City Data for chart///////////////			
		$scope.rptCityLabels = [];
		$scope.rptCityData = [];
					
		$http.get('/getRptCityData')
		.then(function(response) {
			data = response.data;
			for (var i = 0; i < data.length; i++) {
				$scope.rptCityLabels[i] = data[i].catagory;
				$scope.rptCityData[i] = data[i].count;
			}
		})
		/////////////////////////////////////////////////////	
		
		//Native Data for chart///////////////			
		$scope.rptNativeLabels = [];
		$scope.rptNativeData = [];
					
		$http.get('/getRptNativeData')
		.then(function(response) {
			data = response.data;
			for (var i = 0; i < data.length; i++) {
				$scope.rptNativeLabels[i] = data[i].catagory;
				$scope.rptNativeData[i] = data[i].count;
			}
		})
		/////////////////////////////////////////////////////		
		
		//Institution City Data for chart///////////////			
		$scope.rptInsCityLabels = [];
		$scope.rptInsCityData = [];
					
		$http.get('/getRptInsCityData')
		.then(function(response) {
			data = response.data;
			for (var i = 0; i < data.length; i++) {
				$scope.rptInsCityLabels[i] = data[i].catagory;
				$scope.rptInsCityData[i] = data[i].count;
			}
		})
		/////////////////////////////////////////////////////	
		
		//Degree Data for chart///////////////			
		$scope.rptDegreeLabels = [];
		$scope.rptDegreeData = [];
					
		$http.get('/getRptDegreeData')
		.then(function(response) {
			data = response.data;
			for (var i = 0; i < data.length; i++) {
				$scope.rptDegreeLabels[i] = data[i].catagory;
				$scope.rptDegreeData[i] = data[i].count;
			}
		})
		/////////////////////////////////////////////////////
		
		document.body.style.cursor='default';
	
		$scope.refreshReport = function() {
			document.body.style.cursor='wait';
			
			$scope.selectedYear = $scope.applicationYear;
			
			//Overall Progress Data for chart///////////////
			$scope.rptOverallProgresslabels = ['Pending', 'Completed'];
			$scope.rptOverallProgressData= [];			
			$http.get('/getOverAllProgress?applicationYear='+$scope.selectedYear)
			.then(function(response) {
				$scope.rptOverallProgressData[0] = response.data[0].assignedCount
				$scope.rptOverallProgressData[1] = response.data[0].completedCount
				
			});	
			
			$scope.labels = [];
			$scope.series = ['Assigned', 'Completed'];
			$scope.reportData = [0,1];
			$scope.reportData[0]=[];
			$scope.reportData[1]=[];			
			$http.get('/getReviewerProgress?applicationYear='+$scope.selectedYear)
			.then(function(response) {
				data = response.data;
				for (var i = 0; i < data.length; i++) {
					$scope.labels[i] = data[i].reviewer;
					$scope.reportData[0][i] = data[i].assignedCount;
					$scope.reportData[1][i] = data[i].completedCount;
				}
			});	
			
			$scope.rptCityLabels = [];
			$scope.rptCityData = [];			
			$http.get('/getRptCityData?applicationYear='+$scope.selectedYear)
			.then(function(response) {
				data = response.data;
				for (var i = 0; i < data.length; i++) {
					$scope.rptCityLabels[i] = data[i].catagory;
					$scope.rptCityData[i] = data[i].count;
				}
			});		
			
			$scope.rptNativeLabels = [];
			$scope.rptNativeData = [];			
			$http.get('/getRptNativeData?applicationYear='+$scope.selectedYear)
			.then(function(response) {
				data = response.data;
				for (var i = 0; i < data.length; i++) {
					$scope.rptNativeLabels[i] = data[i].catagory;
					$scope.rptNativeData[i] = data[i].count;
				}
			});		
			
			$scope.rptInsCityLabels = [];
			$scope.rptInsCityData = [];			
			$http.get('/getRptInsCityData?applicationYear='+$scope.selectedYear)
			.then(function(response) {
				data = response.data;
				for (var i = 0; i < data.length; i++) {
					$scope.rptInsCityLabels[i] = data[i].catagory;
					$scope.rptInsCityData[i] = data[i].count;
				}
			});	
			
			$scope.rptDegreeLabels = [];
			$scope.rptDegreeData = [];			
			$http.get('/getRptDegreeData?applicationYear='+$scope.selectedYear)
			.then(function(response) {
				data = response.data;
				for (var i = 0; i < data.length; i++) {
					$scope.rptDegreeLabels[i] = data[i].catagory;
					$scope.rptDegreeData[i] = data[i].count;
				}
			});			
			document.body.style.cursor='default';
		};

	} ]);
		
eduApp.controller('EduAppMyAssignmentCtrl', [
		'$scope',
		'$http',
		'$window',
		'$state',
		'$rootScope',
		function($scope, $http, $window, $state, $rootScope) {
			document.body.style.cursor='wait';
			
			
			// initial data load to load my assignments
			$http.get('/loadMyEduApplications')
			.then(function(response) {
				data = response.data;
				$scope.appls = data;
				var arrayLength = $scope.appls.length;
				for (var i = 0; i < arrayLength; i++) {
					$scope.appls[i].crTS = new Date($scope.appls[i].crTS);
				}
				$rootScope.authenticated = true;
			})
			.finally(function () {
				document.body.style.cursor='default';
			});

			// initial sorting by application submission(received) time
			$scope.sort = {
				column : 'crTs',
				descending : true
			};
			
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

		} ]);

eduApp.controller('EduAppProcessAppCtrl', function($scope, $http, $window, $state, $stateParams) {
    // ..
    var applId = $stateParams.applId; 

    //$scope.state = $state.current
    //$scope.params = $stateParams; 
	document.body.style.cursor='wait';   
	
	$http.get('/getActiveUsers')
	.then(function(response) {
		$scope.activeUsers = response.data;
	})
		
	$http.get('/loadEduApplication?applId='+ applId)
	.then(function(response) {
		data = response.data;
		$scope.appl = data[0];
		$scope.potentialDup = data[1];
		$scope.potentialOtherYearApps = data[2];			
	}, function(error){
		if(error.status === 401 || error.status === 403){
			$state.go('accessDenied', {}, {location: false});
		}
	})
	.finally(function () {
		document.body.style.cursor='default';
	});
	
	// function to submit changes made to application processing detail
	$scope.submitEduApplProcessDetail = function() {
		pd = $scope.appl.eduappProcessDetail;
		message = "";
		stopSave = false;
		if (pd.reviewComplete == 'Y') {
			if (pd.reviewerReject != "Y") {
				if (pd.reviewedMarkPercent == null || 
						pd.reviewedAnnualFamilyIncome == null ||
						pd.reviewedAnnualTutionFee == null ||
						pd.reviewedApplCompletePercent == null ||
						pd.reviewerPrefPercent == null) {
					message = 'Complete all required fields with valid data.'
				}
			}
			if ((pd.useSwift == 'Y' && pd.pBankSwiftCode == null) ||
				(pd.useSwift != 'Y' && (pd.pBranchAddressLine1 == null ||
									pd.pBranchAddressLine3 == null))){
				message = message + '\n' + 'Bank Details Incomplete.'
			}
			//message = 'Recent Aggregate % required. ';
			//message = message + 'Annual Family Income required. ';
			
			if (message != "") {
				$window.alert(message);
				$window.alert('Changes not Saved');
				stopSave = true;				
			}
		}
		if (stopSave == false) {
			document.body.style.cursor='wait';  
			$http.post('/submitEduApplProcessDetail', $scope.appl.eduappProcessDetail)
			.then(function(response) {
				$window.alert('Data Saved Successfully');
			})
			.finally(function () {
				document.body.style.cursor='default';
			});
		}
	};
	
    
})


eduApp.controller('EduAppNewApplicationsCtrl', [
   		'$scope',
   		'$http',
   		'$window',
   		'$state',
   		function($scope, $http, $window, $state) {
   			document.body.style.cursor='wait';
   			
   			$http.get('/getActiveUsers')
   			.then(function(response) {
   				$scope.activeUsers = response.data;
   			})
   			
   			// initial data load to load all
			// applications in status 'New'
   			$http.get('/loadNewEduApplications')
   			.then(function(response) {
   				$scope.appls = response.data;
				var arrayLength = $scope.appls.length;
				for (var i = 0; i < arrayLength; i++) {
					$scope.appls[i].crTS = new Date($scope.appls[i].crTS);
				}   				
   			}, function(error){
   				console.log('new app load Failed');
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
				column : 'crTs',
				descending : true
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
		} ]);

eduApp.controller('EduAppSearchCtrl', [
		'$scope',
		'$http',
		'$window',
		'$state',
		function($scope, $http, $window, $state) {
			
			// initial sorting by application submission(received) time
			$scope.sort = {
				column : 'crTs',
				descending : true
			};

			$scope.memberSearchApplication = function() {
				document.body.style.cursor='wait';
				document.getElementById("searchButton").style.cursor = 'wait';
				document.getElementById("searchButton").disabled = true;

				
				var serializedData = $.param({
					searchNameIdEmail : $scope.searchNameIdEmail
				});

				$http({
					method : 'POST',
					url : '/memberSearchApplication',
					data : serializedData,
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).then(function(response) {
					$scope.appls = response.data;
				},function(error){
					console.log('search Failed');
					if(error.status === 401 || error.status === 403){
						$state.go('accessDenied', {}, {location: false});
					}
				}).finally(function() {
				    document.body.style.cursor='default';
				    document.getElementById("searchButton").style.cursor='pointer';
					document.getElementById("searchButton").disabled = false;					
				});
			};
			
			
			$scope.searchApplication = function() {
				document.body.style.cursor='wait';
				document.getElementById("searchButton").style.cursor = 'wait';
				document.getElementById("searchButton").disabled = true;

				
				var serializedData = $.param({
					searchReviewer : $scope.searchReviewer,
					searchAppYear : $scope.searchAppYear,
					searchNameIdEmail : $scope.searchNameIdEmail
				});

				$http({
					method : 'POST',
					url : '/searchEduApplication',
					data : serializedData,
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).then(function(response) {
					$scope.appls = response.data;
				},function(error){
					console.log('search Failed');
					if(error.status === 401 || error.status === 403){
						$state.go('accessDenied', {}, {location: false});
					}
				}).finally(function() {
				    document.body.style.cursor='default';
				    document.getElementById("searchButton").style.cursor='pointer';
					document.getElementById("searchButton").disabled = false;					
				});
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

			$scope.downloadFile = function(attachmentId) {

				// $window.open($http.post('/downloadAttachment',attachmentId));
				$window
						.open('/downloadAttachment?attachmentId='
								+ attachmentId);
			};

			$scope.submitEduApplProcessDetail = function(processDetail) {
				$http.post('/submitEduApplProcessDetail', processDetail)
						.then(function(response) {
							$window.alert('Data Saved Successfully');
						});
			};
			
		} ]);

eduApp.controller('changePasswdCtrl',  function( $scope, $http, $state) {
	$scope.changePasswd={currentPasswd:"", newPasswd:""};
	$scope.changePasswdSuccess="";
	$scope.confirmChangePasswd = function() {
		console.log($scope.changePasswdSuccess);
		$http.post('/confirmChangePasswd', $scope.changePasswd)
			.then(function(response) {
				$scope.changePasswdSuccess=response.data;
				if (!$scope.changePasswdSuccess){
					$scope.error=true;
				} else {
					$scope.error = false;
				}
				console.log(data);
		  });
	}
});

eduApp.controller('reportsCtrl',  function( $scope, $http, $window) {
	
	$http.get('/public/getEduAppConfig')
	.then(function(response) {
		$scope.currentYear = response.data.appYear;
		$scope.report1Year = $scope.currentYear;
		$scope.report1passcode = "";
		$scope.report1passwd = "";
		
		$scope.report2awdref = "";
		$scope.report2passcode = "";
		$scope.report2passwd = "";	
		
		$scope.report3awdref = "";		
		$scope.report3passcode = "";
		$scope.report3passwd = "";		
		
		$scope.report4passcode = "";
	});		
	
	$scope.bankInterfaceDataDownload = function() {
		$window.open('/downloadAppWithReviewerData?passcode=' +	$scope.report1passcode 
				+ '&passwd='+$scope.report1passwd
				+ '&reportYear='+$scope.report1Year);
	}
	
	$scope.bankTransferInstructionDataDownload = function() {
		$window.open('/downloadBankTransferInstructionData?passcode=' +	$scope.report2passcode 
				+ '&passwd='+$scope.report2passwd
				+ '&awdref='+$scope.report2awdref);
	}
	
	$scope.awardMailingAddressDataDownload = function() {
		$window.open('/downloadAwardMailingAddressData?passcode=' +	$scope.report3passcode
		+ '&passwd='+$scope.report3passwd
		+ '&awdref='+$scope.report3awdref);		
	}

	$scope.rankingForHighAwardReviewDataDownload = function() {
			$window.open('/downloadRankingForHighAwardReviewData?passcode=' + $scope.report4passcode);			
		
  /* testing http post for window.open
		$scope.serializedData ={passcode:$scope.report3passcode, passwd:$scope.report3passwd, awdref:$scope.report3awdref };

		console.log($scope.serializedData);
		var popup = $window.open('','downloadData');
		popup.document.write('loading ...');
		
		$http.post('/downloadAwardMailingAddressData', $scope.serializedData).then(function(r){
			console.log(r.data.url);
		    popup.location.href = "https://www.google.com/";
		});		
  */

	}
	
});

eduApp.controller('forgotCtrl',  function( $scope, $http, $state) {
	$scope.email="";

	$scope.forgot = function() {
		document.body.style.cursor='wait';
		$http.post('/forgotPasswd?email='+ $scope.email)
			.then(function(response) {
				$scope.forgotEmailSuccess=response.data;
				if (!$scope.forgotEmailSuccess){
					$scope.error=true;
					$scope.emailSent = false;
				} else {
					$scope.error = false;
					$scope.emailSent = true;
				}
			}).finally(function () {
				document.body.style.cursor='default';
			});
	}
});

eduApp.controller('resetCtrl',  function( $scope, $http, $state, $stateParams) {
    var token = $stateParams.token; 
	$scope.newPasswd="";

	$scope.reset = function() {
		$http.post('/resetPasswd?token='+ token + '&newPass=' + $scope.newPasswd)
			.then(function(response) {
				$scope.resetPassSuccess=response.data;
				if (!$scope.resetPassSuccess){
					$scope.error=true;
					$scope.resetSuccess = false;
				} else {
					$scope.error = false;
					$scope.resetSuccess = true;
				}
		  });
	}
});

eduApp.component('applicationDisplay', {
		bindings: {displayAppl: '<'},
		templateUrl: 'templates/committee/c_applicationDataDisplayPanels.html',
		controller: function ($window) {
			// function to download attachment 
			this.downloadFile = function(attachmentId) {
				$window.open('/downloadAttachment?attachmentId=' + attachmentId);
			};
		}
});

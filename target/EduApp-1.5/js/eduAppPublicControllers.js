eduApp.controller('EduApplicationCtrl', [
		'$scope',
		'$http',
		'Upload',
		'$timeout',
		'$state',
		'$stateParams',
		'$filter',
		function($scope, $http, Upload, $timeout, $state, $stateParams, $filter) {
		
			// $scope.message = 'welcome';
			
			var dob, dobString, dobString3
			if (typeof($stateParams.birthDate) != "undefined") {
/*// Needed when using input type date
				dob = new Date($stateParams.birthDate);
					
				var year = dob.getFullYear();
				var month = (1 + dob.getMonth()).toString();
				month = month.length > 1 ? month : '0' + month;
				var day = dob.getDate().toString();
				day = day.length > 1 ? day : '0' + day;
				   				
				dobString = month + '/' + day + '/' + year;
*/
				console.log($stateParams.birthDate); //in 'dd/mm/yyyy' format
				dobString = $stateParams.birthDate;
			}; 
			
			
			var serializedData = $.param({
				oldStudent : $stateParams.oldStudent,
				studentId : $stateParams.studentId,
				birthDate : dobString
			});
			
			document.body.style.cursor='wait';
			$http({
				method : 'POST',
				url : '/public/getBlankEduApplication',
				data : serializedData,
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).then(function(response) {
					$scope.appl = response.data;
					if ($stateParams.oldStudent == 1) {
						console.log($scope.appl.birthdate);
						if (!$scope.appl.birthdate) {

							$scope.pageErrMsg = "Invalid StudentId and Date of Birth. If " +
									"you have a valid NSNA StudentID please try again. " +
									"Continuing to submit the application will result in " +
									"treating you as new student and may delay your application processing.";

						}
					}
			})
			.finally(function () {
				document.body.style.cursor='default';
			});

			/*
			 * $http.get('/public/getBlankEduApplication').success(function(data) {
			 * $scope.appl = data; });
			 */
			
			$scope.cityOptions = [ {
				value : 'AMARAVATHIPUDUR',
				text : 'AMARAVATHIPUDUR'
			}, {
				value : 'AMBATTUR',
				text : 'AMBATTUR'
			}, {
				value : 'ANNANUR',
				text : 'ANNANUR'
			}, {
				value : 'ARIMALAM(PO)',
				text : 'ARIMALAM(PO)'
			}, {
				value : 'ARIYALUR',
				text : 'ARIYALUR'
			}, {
				value : 'ASHOKAPURAM',
				text : 'ASHOKAPURAM'
			}, {
				value : 'ATHANGUDI',
				text : 'ATHANGUDI'
			}, {
				value : 'ATHUR',
				text : 'ATHUR'
			}, {
				value : 'AVINASHI',
				text : 'AVINASHI'
			}, {
				value : 'BUTHALUR TK',
				text : 'BUTHALUR TK'
			}, {
				value : 'CHENNAI',
				text : 'CHENNAI'
			}, {
				value : 'CHIDAMBARAM',
				text : 'CHIDAMBARAM'
			}, {
				value : 'CHOKKANATHAPURAM',
				text : 'CHOKKANATHAPURAM'
			}, {
				value : 'COIMBATORE',
				text : 'COIMBATORE'
			}, {
				value : 'CUDDALORE',
				text : 'CUDDALORE'
			}, {
				value : 'DEVAKOTTAI',
				text : 'DEVAKOTTAI'
			}, {
				value : 'DHARMAPURI',
				text : 'DHARMAPURI'
			}, {
				value : 'DINDIGUL',
				text : 'DINDIGUL'
			}, {
				value : 'ERODE',
				text : 'ERODE'
			}, {
				value : 'GANAPATHY',
				text : 'GANAPATHY'
			}, {
				value : 'GOBICHETTIPALAYAM',
				text : 'GOBICHETTIPALAYAM'
			}, {
				value : 'GOMATHIPURAM',
				text : 'GOMATHIPURAM'
			}, {
				value : 'HOSUR',
				text : 'HOSUR'
			}, {
				value : 'HYDERABAD',
				text : 'HYDERABAD'
			}, {
				value : 'KADIYAPATTI',
				text : 'KADIYAPATTI'
			}, {
				value : 'KALLAL',
				text : 'KALLAL'
			}, {
				value : 'KANADAVARAYANPATTI',
				text : 'KANADAVARAYANPATTI'
			}, {
				value : 'KANDANUR',
				text : 'KANDANUR'
			}, {
				value : 'KARAIKAL',
				text : 'KARAIKAL'
			}, {
				value : 'KARAIKUDI',
				text : 'KARAIKUDI'
			}, {
				value : 'KARUMATHAMPATTI',
				text : 'KARUMATHAMPATTI'
			}, {
				value : 'KARUR',
				text : 'KARUR'
			}, {
				value : 'KELAMBAKKAM,KANCHIPURAM DIST',
				text : 'KELAMBAKKAM,KANCHIPURAM DIST'
			}, {
				value : 'KILASIVALPATTI',
				text : 'KILASIVALPATTI'
			}, {
				value : 'KOMARAPALAYAM',
				text : 'KOMARAPALAYAM'
			}, {
				value : 'KONAPET',
				text : 'KONAPET'
			}, {
				value : 'KOTTAIYUR',
				text : 'KOTTAIYUR'
			}, {
				value : 'KUMBAKONAM',
				text : 'KUMBAKONAM'
			}, {
				value : 'KURUVIKONDANPATTI',
				text : 'KURUVIKONDANPATTI'
			}, {
				value : 'LALGUDI',
				text : 'LALGUDI'
			}, {
				value : 'MADAVARAM',
				text : 'MADAVARAM'
			}, {
				value : 'MADURAI',
				text : 'MADURAI'
			}, {
				value : 'MANAPPARAI',
				text : 'MANAPPARAI'
			}, {
				value : 'MANNARGUDI',
				text : 'MANNARGUDI'
			}, {
				value : 'MARAIMALAI NAGAR',
				text : 'MARAIMALAI NAGAR'
			}, {
				value : 'MAYILADUTHURAI',
				text : 'MAYILADUTHURAI'
			}, {
				value : 'MEDAVAKKAM',
				text : 'MEDAVAKKAM'
			}, {
				value : 'MELAISIVAPURI',
				text : 'MELAISIVAPURI'
			}, {
				value : 'MYILADUTHURAI',
				text : 'MYILADUTHURAI'
			}, {
				value : 'MYLAPORE',
				text : 'MYLAPORE'
			}, {
				value : 'MYSORE',
				text : 'MYSORE'
			}, {
				value : 'NACHANDUPATTI',
				text : 'NACHANDUPATTI'
			}, {
				value : 'NALLUR',
				text : 'NALLUR'
			}, {
				value : 'NEDUNGADU',
				text : 'NEDUNGADU'
			}, {
				value : 'NEYVELI',
				text : 'NEYVELI'
			}, {
				value : 'O.SIRUVAYAL,KARAIKUDI',
				text : 'O.SIRUVAYAL,KARAIKUDI'
			}, {
				value : 'ODDANCHATRAM',
				text : 'ODDANCHATRAM'
			}, {
				value : 'OKKUR',
				text : 'OKKUR'
			}, {
				value : 'PALANI',
				text : 'PALANI'
			}, {
				value : 'PALAVANGUDI',
				text : 'PALAVANGUDI'
			}, {
				value : 'PALLATHUR, SIVAGANGAI',
				text : 'PALLATHUR, SIVAGANGAI'
			}, {
				value : 'PAPANASAM',
				text : 'PAPANASAM'
			}, {
				value : 'PATTAMANGALAM',
				text : 'PATTAMANGALAM'
			}, {
				value : 'PATTUKKOTTAI',
				text : 'PATTUKKOTTAI'
			}, {
				value : 'PERIYANAICKEN PALAYAM',
				text : 'PERIYANAICKEN PALAYAM'
			}, {
				value : 'PERUNDURAI',
				text : 'PERUNDURAI'
			}, {
				value : 'PETYTAVAITHALAI, TRICHY DT',
				text : 'PETYTAVAITHALAI, TRICHY DT'
			}, {
				value : 'PILLAIYARPATTI',
				text : 'PILLAIYARPATTI'
			}, {
				value : 'POLLACHI',
				text : 'POLLACHI'
			}, {
				value : 'PON PUDUPATTI',
				text : 'PON PUDUPATTI'
			}, {
				value : 'PONNAMARAVATHY',
				text : 'PONNAMARAVATHY'
			}, {
				value : 'POOLANKURICHI',
				text : 'POOLANKURICHI'
			}, {
				value : 'PORUR,CHENNAI',
				text : 'PORUR,CHENNAI'
			}, {
				value : 'PUDUCHERRY',
				text : 'PUDUCHERRY'
			}, {
				value : 'PUDUKKOTTAI',
				text : 'PUDUKKOTTAI'
			}, {
				value : 'PUDUVAYAL',
				text : 'PUDUVAYAL'
			}, {
				value : 'RAMACHANDRAPURAM POST',
				text : 'RAMACHANDRAPURAM POST'
			}, {
				value : 'RAYAVARAM',
				text : 'RAYAVARAM'
			}, {
				value : 'SALEM',
				text : 'SALEM'
			}, {
				value : 'SECUNDERABAD',
				text : 'SECUNDERABAD'
			}, {
				value : 'SHANMUGANATHAPURAM',
				text : 'SHANMUGANATHAPURAM'
			}, {
				value : 'SHOLINGHUR',
				text : 'SHOLINGHUR'
			}, {
				value : 'SINGANALLUR',
				text : 'SINGANALLUR'
			}, {
				value : 'SIRAYAVAYAL',
				text : 'SIRAYAVAYAL'
			}, {
				value : 'SIRKALI',
				text : 'SIRKALI'
			}, {
				value : 'SIVAGANGAI',
				text : 'SIVAGANGAI'
			}, {
				value : 'SOMANUR',
				text : 'SOMANUR'
			}, {
				value : 'SOWRIPALAYAM',
				text : 'SOWRIPALAYAM'
			}, {
				value : 'SRIRENGAM',
				text : 'SRIRENGAM'
			}, {
				value : 'THANJAVUR',
				text : 'THANJAVUR'
			}, {
				value : 'THIRUPPARANKUNDRAM',
				text : 'THIRUPPARANKUNDRAM'
			}, {
				value : 'THIRUPPATUR',
				text : 'THIRUPPATUR'
			}, {
				value : 'THIRUPPUDANAM',
				text : 'THIRUPPUDANAM'
			}, {
				value : 'THIRUVOTTIYUR',
				text : 'THIRUVOTTIYUR'
			}, {
				value : 'THUDIAYALUR',
				text : 'THUDIAYALUR'
			}, {
				value : 'TIRUMANGALAM',
				text : 'TIRUMANGALAM'
			}, {
				value : 'TIRUNELVELI',
				text : 'TIRUNELVELI'
			}, {
				value : 'TIRUPPUR',
				text : 'TIRUPPUR'
			}, {
				value : 'TIRUVANNAMALAI',
				text : 'TIRUVANNAMALAI'
			}, {
				value : 'TIRUVOTTIYUR',
				text : 'TIRUVOTTIYUR'
			}, {
				value : 'TRICHY',
				text : 'TRICHY'
			}, {
				value : 'TUTICORIN',
				text : 'TUTICORIN'
			}, {
				value : 'UDUMALAIPETTAI,TIRUPPUR DIST',
				text : 'UDUMALAIPETTAI,TIRUPPUR DIST'
			}, {
				value : 'ULAGAMPATTI',
				text : 'ULAGAMPATTI'
			}, {
				value : 'VALAYAPATTI,PONNAMARAVATHY',
				text : 'VALAYAPATTI,PONNAMARAVATHY'
			}, {
				value : 'VALAYAPETTAI',
				text : 'VALAYAPETTAI'
			}, {
				value : 'VELLORE DISTRICT',
				text : 'VELLORE DISTRICT'
			}, {
				value : 'VIRACHILAI',
				text : 'VIRACHILAI'
			}, {
				value : 'WORAIYUR',
				text : 'WORAIYUR'
			} ];
			
			$scope.degreeSpecializationOptions = [ {
				value : 'Accounting',
				text : 'Accounting'
			}, {								
				value : 'Aeronautical',
				text : 'Aeronautical'
			}, {
				value : 'Agriculture',
				text : 'Agriculture'
			}, {				
				value : 'Automobile',
				text : 'Automobile'
			}, {
				value : 'Biomedical',
				text : 'Biomedical'		
			}, {
				value : 'Biotechnology',
				text : 'Biotechnology'					
			}, {
				value : 'Chemical',
				text : 'Chemical'
			}, {
				value : 'Civil',
				text : 'Civil'
			}, {
				value : 'Commerce',
				text : 'Commerce'
			}, {				
				value : 'Computer Science',
				text : 'Computer Science'
			}, {
				value : 'Electrical and Electronics',
				text : 'Electrical and Electronics'
			}, {
				value : 'Electronics and Communication',
				text : 'Electronics and Communication'
			}, {
				value : 'Electronics and Instrumentation',
				text : 'Electronics and Instrumentation'
			}, {
				value : 'Information Technology',
				text : 'Information Technology'					
			}, {
				value : 'Instrumentation and Control',
				text : 'Instrumentation and Control'
			}, {
				value : 'Marine',
				text : 'Marine'
			}, {
				value : 'Mechanical',
				text : 'Mechanical'
			}, {
				value : 'Nursing',
				text : 'Nursing'
			}, {				
				value : 'Pharmacy',
				text : 'Pharmacy'
			}, {				
				value : 'Production',
				text : 'Production'
			}, {
				value : 'Physiotherapy',
				text : 'Physiotherapy'
			}, {				
				value : 'Textile Technology',
				text : 'Textile Technology'
			}, {
				value : 'Petroleum',
				text : 'Petroleum'
			}, {
				value : 'Plastics Technology',
				text : 'Plastics Technology'
			}];

			
			$scope.fatherOccupOptionsConfig = {
				create : true,
				maxItems : 1,
				highlight : true,
				placeholder : "Select/Type Father's Occupaion (profession, job)"
			};
			
			$scope.motherOccupOptionsConfig = {
					create : true,
					maxItems : 1,
					highlight : true,
					placeholder : "Select/Type Mother's Occupaion (profession, job)"
				};			
			
			$scope.occupationOptions = [ {
				value : 'Unemployed',
				text : 'Unemployed (Not Worker)'
			}, {
				value : 'House Wife',
				text : 'House Wife'
			}, {
				value : 'Accountant',
				text : 'Accountant'
			}, {
				value : 'Administration Professional',
				text : 'Administration Professional'
			}, {
				value : 'Bank Employee',
				text : 'Bank Employee'	
			}, {				
				value : 'Banker (Money Lending)',
				text : 'Banker (Money Lending)'	
			}, {
				value : 'Beautician',
				text : 'Beautician'	
			}, {					
				value : 'Own Business',
				text : 'Own Business  (like medical shop, retail store etc)'
			}, {
				value : 'Chartered Accountant',
				text : 'Chartered Accountant'
			}, {
				value : 'Clerical Official',
				text : 'Clerical Official'
			}, {				
				value : 'Construction Worker',
				text : 'Construction Worker'
			}, {	
				value : 'Designer',
				text : 'Designer'
			}, {				
				value : 'Driver',
				text : 'Driver'
			}, {				
				value : 'Electrician',
				text : 'Electrician'
			}, {				
				value : 'Engineer',
				text : 'Engineer'
			}, {
				value : 'Factory worker',
				text : 'Factory worker'
			}, {
				value : 'Farmer',
				text : 'Farmer'
			}, {					
				value : 'Finance Professional',
				text : 'Finance Professional'
			}, {				
				value : 'Government Service',
				text : 'Government Service'
			}, {
				value : 'HealthCare Professional',
				text : 'HealthCare Professional'
			}, {
				value : 'Hotel  Restaurant Professional',
				text : 'Hotel  Restaurant Professional'
			}, {	
				value : 'Marketing Professional',
				text : 'Marketing Professional'
			}, {				
				value : 'Mechanic',
				text : 'Mechanic'	
			}, {
				value : 'Nurse',
				text : 'Nurse'	
			}, {
				value : 'Occupational Therapist',
				text : 'Occupational Therapist'	
			}, {				
				value : 'Optician',
				text : 'Optician'	
			}, {				
				value : 'Pharmacist',
				text : 'Pharmacist'	
			}, {
				value : 'Physician Assistant',
				text : 'Physician Assistant'	
			}, {	
				value : 'Professor',
				text : 'Professor'	
			}, {				
				value : 'RealEstate Professional',
				text : 'RealEstate Professional'		
			}, {				
				value : 'Retired',
				text : 'Retired'		
			}, {
				value : 'Sales Professional',
				text : 'Sales Professional'		
			}, {				
				value : 'Shop Worker',
				text : 'Shop Worker'		
			}, {
				value : 'Teacher - School',
				text : 'Teacher - School'		
			}, {	
				value : 'Technician',
				text : 'Technician'		
			}, {				
				value : 'Transportation Professional',
				text : 'Transportation Professional'	
			}, {	
				value : 'Writer',
				text : 'Writer'					
			}];				
			
			
			$scope.degreeOptionsConfig = {
					create : true,
					maxItems : 1,
					highlight : true,
					placeholder : "Select (Type if not in list) Degree"
				};
			
			$scope.degreeOptions = [ {
				value : 'BE',
				text : 'BE'
			}, {
				value : 'BTECH',
				text : 'BTech'
			}, {
				value : 'BCOM',
				text : 'BCom'
			}, {
				value : 'BSC',
				text : 'BSc'
			}, {
				value : 'BCA',
				text : 'BCA'
			}, {
				value : 'MBA',
				text : 'MBA'
			}, {
				value : 'DCE',
				text : 'DCE'
			}, {
				value : 'DME',
				text : 'DME'
			}, {
				value : 'MBBS',
				text : 'MBBS'
			}, {
				value : 'MSC',
				text : 'MSc'
			}, {
				value : 'BA',
				text : 'BA'		
			}, {
				value : 'DIPLOMA',
				text : 'DIPLOMA'		
			}, {
				value : 'MCA',
				text : 'MCA'		
			}, {
				value : 'ME',
				text : 'ME'		
			}, {
				value : 'BARCH',
				text : 'BArch'		
			}, {
				value : 'BPHARM',
				text : 'BPharm'		
			}, {
				value : 'CA',
				text : 'CA'
			}, {
				value : 'POLYTECHNIC',
				text : 'POLYTECHNIC'
			}];					

			$scope.universityOptions = [ {
				value : 'Alagappa University',
				text : 'Alagappa University'
			}, {
				value : 'Anna University',
				text : 'Anna University'
			}, {
				value : 'Annamalai University',
				text : 'Annamalai University'
			}, {
				value : 'Bharathiar University',
				text : 'Bharathiar University'
			}, {
				value : 'Bharathidasan University',
				text : 'Bharathidasan University'
			}, {
				value : 'Bharathidasan Institute of Management',
				text : 'Bharathidasan Institute of Management'					
			}, {
				value : 'Indian Institute of Management Tiruchirappalli',
				text : 'Indian Institute of Management Tiruchirappalli'					
			}, {
				value : 'Indian Institute of Technology Madras',
				text : 'Indian Institute of Technology Madras'
			}, {
				value : 'Madurai Kamaraj University',
				text : 'Madurai Kamaraj University'
			}, {
				value : 'Manonmaniam Sundaranar University',
				text : 'Manonmaniam Sundaranar University'
			}, {
				value : 'Mother Teresa Women\'s University',
				text : 'Mother Teresa Women\'s University'
			}, {
				value : 'National Institute of Technology Tiruchirappalli',
				text : 'National Institute of Technology Tiruchirappalli'
			}, {
				value : 'National Institute of Technology, Puducherry',
				text : 'National Institute of Technology, Puducherry'					
			}, {
				value : 'The Tamil Nadu Dr. M. G. R. Medical University',
				text : 'The Tamil Nadu Dr. M. G. R. Medical University'
			}, {
				value : 'Tamil Nadu Dr. Ambedkar Law University',
				text : 'Tamil Nadu Dr. Ambedkar Law University'
			}, {
				value : 'Tamil Nadu Agricultural University',
				text : 'Tamil Nadu Agricultural University'
			}, {
				value : 'Periyar University',
				text : 'Periyar University'
			}, {
				value : 'Tamil Nadu Veterinary and Animal Sciences University',
				text : 'Tamil Nadu Veterinary and Animal Sciences University'
			}, {
				value : 'Tamil University',
				text : 'Tamil University'
			}, {
				value : 'Thiruvalluvar University',
				text : 'Thiruvalluvar University'
			}, {
				value : 'University of Madras',
				text : 'University of Madras'
			}, {
				value : 'Tamil Nadu Horticulture University',
				text : 'Tamil Nadu Horticulture University'
			}, {
				value : 'Tamil Nadu Fisheries University',
				text : 'Tamil Nadu Fisheries University'
			}];	

			$scope.myConfig = {
				create : true,
				maxItems : 1,
				highlight : true,
				placeholder : ''
			};

			$scope.uploadFilesAndSubmit = function() {

				document.body.style.cursor='wait';
				document.getElementById("submitButton").style.cursor = 'wait';
				document.getElementById("submitButton").disabled = true;
				
				
				// fileUpload function as promise
				var fileUpload = Upload.upload({
						url : '/public/submitWithFile',
						data : {
							photo:$scope.photo,
							markSheet1 : $scope.markSheet1,
							markSheet2 : $scope.markSheet2,
							tuitionReceipt1 : $scope.tuitionReceipt1,
							tuitionReceipt2 : $scope.tuitionReceipt2,						
							incomeProof : $scope.incomeProof,
							nagaratharProof : $scope.nagaratharProof,
							scholarshipLetter : $scope.scholarshipLetter,
							application : angular.toJson($scope.appl)
						}
					});

				fileUpload.then(function(response) {
					$timeout(function() {
						fileUpload.result = response.data;
					});

					$state.go('scholarshipApp.applicationConfirmation');
				}, function(response) {
					if (response.status > 0)
						$scope.errorMsg = response.status + ': '
								+ response.data;
				}, function(evt) {
					// Math.min is to fix IE which reports 200% sometimes
					fileUpload.progress = Math.min(100, parseInt(100.0
							* evt.loaded / evt.total));
				});
				
				fileUpload.finally(function() {
					document.body.style.cursor='default';
					document.getElementById("submitButton").style.cursor = 'pointer';
					document.getElementById("submitButton").disabled = false;					
				});

			};
			
			
			/*
			 * $scope.getReqParams = function () { return
			 * $scope.generateErrorOnServer ? '?errorCode=' +
			 * $scope.serverErrorCode + '&errorMessage=' + $scope.serverErrorMsg :
			 * ''; };
			 */
			$scope.SubmitApplication = function() {
				$http.post('/submitEduApplication', $scope.appl).then(
						function(response) {

						});
			};
		} ]);

eduApp.controller('EduAppConfirmationCtrl', [ '$scope', '$http',
		function($scope, $http) {
			$http.get('/public/getConfirmationData').then(function(response) {
				$scope.confirmationData = response.data;

			});
		} ]);

eduApp.controller('PublicSideBarCtrl', [ '$scope', '$http',
  		function($scope, $http) {
  			$http.get('/public/getEduAppConfig')
  			.then(function(response) {
  				$scope.eduappConfig = response.data;
   			});
  			
  			$scope.updateSwiftCodeStep1 = function() {
  				var dob, dobString	
  				if (typeof($scope.birthDate) != "undefined") {
  					dobString = $scope.birthDate //in 'dd/mm/yyyy' format
  				}; 
  				var serializedData = $.param({
  					confirmationNmbr : $scope.confirmationNmbr,
  					studentId : $scope.studentId,
  					birthDate : dobString
  				});
  				$http({
  					method : 'POST',
  					url : '/public/updateSwiftCodeStep1',
  					data : serializedData,
  					headers : {
  						'Content-Type' : 'application/x-www-form-urlencoded'
  					}
  				})
  				.then(function(response) {
  						$scope.step2Data = response.data;
  						$scope.screen=2;
  				})
  				.finally(function () {
  					document.body.style.cursor='default';
  				});
  			}
  			
 			$scope.backToStep1 = function() {
 				$scope.step2Data = '';
 				$scope.screen=1;
 				$scope.birthDate='';
 				$scope.studentId='';
 				//$scope.swiftCode='';
 				$scope.confirmationNmbr=''; 				
 				$scope.reEnterBankSwiftCode="";
 				$scope.pageErrMsg="";
 				
 			} 
 			
  			$scope.updateSwiftCodeStep2 = function() {
  				//Swift Code validation
  				$scope.pageErrMsg="";
  				var tempSwiftCode = $scope.step2Data.swiftCode;
  				if ( (tempSwiftCode.length !=8 ) &&
  					(tempSwiftCode.length !=11) ) {
  					//window.alert("Invalid Swift Code: Length should be 8 or 11 characters");
  					$scope.pageErrMsg = "Invalid Swift Code: Length should be 8 or 11 characters";
  					return;
  				}
  				if  (tempSwiftCode.substr(4,2) != "IN") {
  	  					//window.alert("Invalid Swift Code");
  	  					$scope.pageErrMsg = "Invalid Swift Code";
  	  					return;
  	  			}  				
  				
  				
  				var serializedData = $.param({
  					confirmationNmbr : $scope.step2Data.confirmationNmbr,
  					swiftCode : $scope.step2Data.swiftCode
  				});

  				$http({
  					method : 'POST',
  					url : '/public/updateSwiftCodeStep2',
  					data : serializedData,
  					headers : {
  						'Content-Type' : 'application/x-www-form-urlencoded'
  					}
  				})
  				.then(function(response) {
  					$scope.step2Data = '';
  	 				$scope.screen=3;
  	 				//$scope.swiftCode='';
  	 				$scope.reEnterBankSwiftCode="";
  				})
  				.finally(function () {
  					document.body.style.cursor='default';
  				});
  			}
 			
 				
  		} ]);

eduApp.controller('PublicAppInstructionCtrl', [ '$scope', '$http',
       		function($scope, $http) {
       			$http.get('/public/getEduAppConfig')
       			.then(function(response) {
       				$scope.eduappConfig = response.data;
        			});
  			}
   	 ]);

eduApp.controller('EduAppStatusCtrl', function($scope, $http, $state, $stateParams) {
	document.body.style.cursor='wait';
	
	var dob, dobString	
	if (typeof($stateParams.birthDate) != "undefined") {
	/*// Needed when using input type date				
		dob = new Date($stateParams.birthDate);
		
		var year = dob.getFullYear();
		var month = (1 + dob.getMonth()).toString();
		month = month.length > 1 ? month : '0' + month;
		var day = dob.getDate().toString();
		day = day.length > 1 ? day : '0' + day;
		   				
		dobString = month + '/' + day + '/' + year;
	*/
		dobString = $stateParams.birthDate //in 'dd/mm/yyyy' format
	}; 
	var serializedData = $.param({
		confirmationNmbr : $stateParams.confirmationNmbr,
		studentId : $stateParams.studentId,
		birthDate : dobString
	});
	$http({
		method : 'POST',
		url : '/public/checkApplicationStatus',
		data : serializedData,
		headers : {
			'Content-Type' : 'application/x-www-form-urlencoded'
		}
	})
	.then(function(response) {
			$scope.statusData = response.data;
			$scope.confirmationNmbr = '';
	})
	.finally(function () {
		document.body.style.cursor='default';
	});
			
});
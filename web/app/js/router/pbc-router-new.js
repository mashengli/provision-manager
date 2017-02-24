require('../lib/jquery.simplePagination');

var pbcModule = require("../module/pbc-module-new");
var commonModule = require("../module/common-module");

module.exports = {
	init: function(){
		avalon.router.add("/pbc-new", function () {
			if(avalon.vmodels['main']!=undefined){
				delete avalon.vmodels['main'];
			}

			var mainVm = avalon.define({
				$id: 'main',
				template: require('../../template/pbc-new.html'),
				data: {
					reportTypeList: [
						{
							label: "汇总报表"
						},
						{
							label: "账户报表"
						}
					],
					selectedReportTypeIndex: 0,
					reportList: [],
                    pageReportList: [],
					checkedReportIndexList: []
				},
				selectReportType: function(){
                    $('#filter').val("");

					mainVm.data.selectedReportTypeIndex = document.getElementsByName("report-type")[0].value;

                    var reportListObj = pbcModule.getReportList(
                        mainVm.data.selectedReportTypeIndex,
						null,
                        null
					);

                    mainVm.data.reportList = reportListObj.reportList;

                    mainVm.data.getReportListByPage(1);
				},
				checkAll: function () {
					mainVm.data.checkedReportIndexList=[];

					if(document.getElementsByName("check-all")[0].checked){
						for(var i=0; i<mainVm.data.pageReportList.length; i++){
							mainVm.data.checkedReportIndexList.push(i);
						}

						for(var i=0; i<document.getElementsByName("check-one").length; i++){
							document.getElementsByName("check-one")[i].checked = true;
						}
					} else {
						for(var i=0; i<document.getElementsByName("check-one").length; i++){
							document.getElementsByName("check-one")[i].checked = false;
						}
					}
				},
				checkOne: function () {
					mainVm.data.checkedReportIndexList=[];

					for(var i=0; i<document.getElementsByName("check-one").length; i++){
						if (document.getElementsByName("check-one")[i].checked) {
							mainVm.data.checkedReportIndexList.push(i);
						}
					}
                },
                unCheckAll: function () {
				    if (mainVm.data.checkedReportIndexList.length!=0) {
                        mainVm.data.checkedReportIndexList = [];
                    }

                    document.getElementsByName("check-all")[0].checked = false;

                    for(var i=0; i<document.getElementsByName("check-one").length; i++){
                        document.getElementsByName("check-one")[i].checked = false;
                    }
                },
                generate: function (index) {
                    $('#filter').val("");

                    var year = $('#monthpicker').html().split("-")[0];
                    var month = $('#monthpicker').html().split("-")[1];

                    var reportList={
                        start_day: commonModule.getStartDay(year, month),
                        end_day: commonModule.getEndDay(year, month),
                        report_type: mainVm.data.selectedReportTypeIndex,
                        report_list: []
                    };

                    reportList.report_list.push({
                        report_name: mainVm.data.pageReportList[index].report_name
                    });

                    var result = pbcModule.generateReport(reportList);

                    if (result) {
                        var reportListObj =pbcModule.getReportList(
                            mainVm.data.selectedReportTypeIndex,
                            null,
                            null
                        );

                        mainVm.data.reportList = reportListObj.reportList;

                        mainVm.data.getReportListByPage(1);
					}
                },
				batchGenerate: function () {
                    $('#filter').val("");

					if (mainVm.data.checkedReportIndexList.length==0){
                        commonModule.errorModal("请选择您要生成的报表!");

                        return;
					}

                    var year = $('#monthpicker').html().split("-")[0];
                    var month = $('#monthpicker').html().split("-")[1];

					var reportList={
                        start_day: commonModule.getStartDay(year, month),
                        end_day: commonModule.getEndDay(year, month),
                        report_type: mainVm.data.selectedReportTypeIndex,
                        report_list: []
					};

                    for (var i = 0; i < mainVm.data.checkedReportIndexList.length; i++) {
                        reportList.report_list.push({
                            report_name: mainVm.data.pageReportList[mainVm.data.checkedReportIndexList[i]].report_name
                        });
                    }

                    var result = pbcModule.generateReport(reportList);

                    if (result) {
                        var reportListObj = pbcModule.getReportList(
                            mainVm.data.selectedReportTypeIndex,
                            null,
                            null
                        );

                        mainVm.data.reportList = reportListObj.reportList;

                        mainVm.data.getReportListByPage(1);
                    }
				},
				submit: function () {
					if($('#monthpicker').html()==''){
                        commonModule.errorModal("请选择月份!");

						return;
					}

					if(avalon.vmodels['submit-controller']!=undefined){
						delete avalon.vmodels['submit-controller'];
					}

                    var year = $('#monthpicker').html().split("-")[0];
                    var month = $('#monthpicker').html().split("-")[1];

					var submitVm = avalon.define({
						$id: 'submit-controller',
                        checkReportFlag: 0,
						submit: function () {
                            pbcModule.submitReport(
                            	commonModule.getStartDay(year, month),
								commonModule.getEndDay(year, month)
							);
						},
                        checkReport: function () {
                            if (document.getElementsByName("check-report")[0].checked) {
                                submitVm.checkReportFlag=1;
                            } else {
                                submitVm.checkReportFlag=0;
                            }
                        }
					});

					var submitTemplate = require("../../template/submit.html");

					$('#modal').html(submitTemplate).modal({fadeDuration: 100});
					avalon.scan(document.getElementById("modal").firstChild);
				},
                download: function (index) {
                    var year = $('#monthpicker').html().split("-")[0];
                    var month = $('#monthpicker').html().split("-")[1];

					pbcModule.download(
						mainVm.data.selectedReportTypeIndex,
                        commonModule.getStartDay(year, month),
                        commonModule.getEndDay(year, month),
                        mainVm.data.pageReportList[index].report_name
					);
                },
				downloadAll: function () {
                    var year = $('#monthpicker').html().split("-")[0];
                    var month = $('#monthpicker').html().split("-")[1];

                    pbcModule.downloadAll(
                        commonModule.getStartDay(year, month),
                        commonModule.getEndDay(year, month)
                    );
				},
				filter: function () {
                    var reportListObj = pbcModule.getReportList(
                        mainVm.data.selectedReportTypeIndex,
                        null,
                        null
                    );

                    mainVm.data.reportList = reportListObj.reportList;

                    mainVm.data.getReportListByPage(1);
				}
			});

            var getReportListByPage = function(index, event){
                if($('#filter').val()!=''){
                    for(var i=0; i<mainVm.data.reportList.length; i++){
                        if (mainVm.data.reportList[i].report_name.indexOf($('#filter').val())<0){
                            mainVm.data.reportList.splice(i, 1);
                            i=i-1;
                        }
                    }
                }

                var pageSize = 10;
                var startPosition = (index-1)*pageSize;
                var endPosition = startPosition+pageSize>=mainVm.data.reportList.length ? mainVm.data.reportList.length : startPosition+pageSize;
                mainVm.data.pageReportList = mainVm.data.reportList.slice(startPosition, endPosition);

                $('#pagination').pagination({
                    items: mainVm.data.reportList.length,
                    currentPage: index,
                    itemsOnPage: pageSize,
                    cssStyle: 'light-theme',
                    prevText: '<',
                    nextText: '>',
                    onPageClick: getReportListByPage
                });

                $('#pagination a').attr("href", "#!/pbc-new");

                mainVm.unCheckAll();
            };

            mainVm.data.getReportListByPage = getReportListByPage;

			var reportListObj = pbcModule.getReportList(0);
            mainVm.data.reportList = reportListObj.reportList;

			mainVm.$watch('onReady', function(){
				$("#filter").keydown(function(event){
                    event = event || window.event;

					if(event.which == "13"){
						mainVm.filter();
					}
				});

                var dateObj = new Date();
                var currentYear = dateObj.getFullYear();

				var years = [];
				for (var i=0; i<=currentYear-2008; i++){
					years.push(currentYear-i);
				}

                $('#monthpicker').monthpicker({
                    years: years,
                    topOffset: 6,
                    onMonthSelect: function(month, year) {
                        $('#filter').val("");

                        var reportListObj = pbcModule.getReportList(
                            mainVm.data.selectedReportTypeIndex,
                            null,
                            null
                        );

                        mainVm.data.reportList = reportListObj.reportList;

                        mainVm.data.getReportListByPage(1);
                    }
                });

                mainVm.data.getReportListByPage(1);
            });

			avalon.scan(document.body);
		});
	}
};
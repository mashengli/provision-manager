var b2eModule = require("../module/b2e-module");

module.exports = {
    init: function(){
        avalon.router.add("/b2e/bank/detail", function () {
            if(avalon.vmodels['main']!=undefined){
                delete avalon.vmodels['main'];
            }

            var mainVm = avalon.define({
                $id: 'main',
                template: require('../../template/b2e-bank-detail.html'),
                data: {
                    bankList: b2eModule.getBankList(),
                    selectedBank: 0,
                    accountList: [],
                    selectedAccount: 0,
                    bankDetailList: [],
                    detailKeyList:[],
                    selectBank: function () {
                        mainVm.data.selectedBank = document.getElementsByName("bank")[0].value;
                        mainVm.data.accountList = b2eModule.getAccountList(mainVm.data.bankList[mainVm.data.selectedBank].Type);
                        mainVm.data.selectedAccount = 0;

                        mainVm.data.getBankDetailListByPage(1);
                    },
                    selectAccount: function () {
                        mainVm.data.selectedAccount = document.getElementsByName("account")[0].value;
                        mainVm.data.getBankDetailListByPage(1);
                    }
                }
            });

            mainVm.data.accountList = b2eModule.getAccountList(mainVm.data.bankList[mainVm.data.selectedBank].Type);

            var getBankDetailListByPage = function(index, event){
                var pageSize = 10;
                var result = b2eModule.getBankDetailList(
                    mainVm.data.bankList[mainVm.data.selectedBank].Type,
                    $("#datetime-start").val(),
                    $("#datetime-end").val(),
                    mainVm.data.accountList[mainVm.data.selectedAccount].AccNo,
                    index,
                    pageSize
                );
                mainVm.data.bankDetailList = result.Items==undefined ? [] : result.Items;

                mainVm.data.detailKeyList = [];
                if (mainVm.data.bankDetailList.length>0){
                    for(var key in mainVm.data.bankDetailList[0]){
                        mainVm.data.detailKeyList.push(key);
                    }
                }

                $('#pagination').pagination({
                    items: result.Total,
                    currentPage: index,
                    itemsOnPage: pageSize,
                    cssStyle: 'light-theme',
                    prevText: '<',
                    nextText: '>',
                    onPageClick: getBankDetailListByPage
                });

                $('#pagination a').attr("href", "#!/b2e/bank/detail");
            };

            mainVm.data.getBankDetailListByPage = getBankDetailListByPage;

            mainVm.$watch('onReady', function(){
                $.datetimepicker.setLocale('ch');

                $('#datetime-start').datetimepicker({
                    timepicker:false,
                    format:'Y-m-d',
                    maxDate:'+1970/01/01',
                    scrollMonth:false,
                    scrollTime:false,
                    scrollInput:false,
                    onShow:function(){
                        this.setOptions({
                            maxDate: $('#datetime-end').val() ? $('#datetime-end').val() : '+1970/01/01'
                        });
                    },
                    onSelectDate: function(){
                        mainVm.data.getBankDetailListByPage(1);
                    }
                });

                $('#datetime-end').datetimepicker({
                    timepicker:false,
                    format:'Y-m-d',
                    maxDate:'+1970/01/01',
                    scrollMonth:false,
                    scrollTime:false,
                    scrollInput:false,
                    onShow:function(){
                        this.setOptions({
                            minDate: $('#datetime-start').val() ? $('#datetime-start').val() : false
                        });
                    },
                    onSelectDate: function(){
                        mainVm.data.getBankDetailListByPage(1);
                    }
                });

                var dateObj = new Date();
                var year = dateObj.getFullYear();
                var month = dateObj.getMonth()+1;
                var day = dateObj.getDate();

                $("#datetime-start").val(year+"-"+month+"-01");
                $("#datetime-end").val(year+"-"+month+"-"+day);

                mainVm.data.getBankDetailListByPage(1);
            });
            avalon.scan(document.body);
        });

        avalon.router.add("/b2e/bank/balance", function () {
            if(avalon.vmodels['main']!=undefined){
                delete avalon.vmodels['main'];
            }

            var mainVm = avalon.define({
                $id: 'main',
                template: require('../../template/b2e-bank-balance.html'),
                data: {
                    bankList: b2eModule.getBankList(),
                    selectedBank: 0,
                    accountList: [],
                    selectedAccount: 0,
                    bankBalanceList: [],
                    balanceKeyList:[],
                    selectBank: function () {
                        mainVm.data.selectedBank = document.getElementsByName("bank")[0].value;
                        mainVm.data.accountList = b2eModule.getAccountList(mainVm.data.bankList[mainVm.data.selectedBank].Type);
                        mainVm.data.selectedAccount = 0;

                        mainVm.data.getBankBalanceListByPage(1);
                    },
                    selectAccount: function () {
                        mainVm.data.selectedAccount = document.getElementsByName("account")[0].value;
                        mainVm.data.getBankBalanceListByPage(1);
                    }
                }
            });

            mainVm.data.accountList = b2eModule.getAccountList(mainVm.data.bankList[mainVm.data.selectedBank].Type);

            var getBankBalanceListByPage = function(index, event){
                var pageSize = 10;
                var result = b2eModule.getBankBalanceList(
                    mainVm.data.bankList[mainVm.data.selectedBank].Type,
                    $("#datetime-start").val(),
                    $("#datetime-end").val(),
                    mainVm.data.accountList[mainVm.data.selectedAccount].AccNo,
                    index,
                    pageSize
                );
                mainVm.data.bankBalanceList = result.Items==undefined ? [] : result.Items;

                mainVm.data.balanceKeyList = [];
                if (mainVm.data.bankBalanceList.length>0){
                    for(var key in mainVm.data.bankBalanceList[0]){
                        mainVm.data.balanceKeyList.push(key);
                    }
                }

                $('#pagination').pagination({
                    items: result.Total,
                    currentPage: index,
                    itemsOnPage: pageSize,
                    cssStyle: 'light-theme',
                    prevText: '<',
                    nextText: '>',
                    onPageClick: getBankBalanceListByPage
                });

                $('#pagination a').attr("href", "#!/b2e/bank/balance");
            };

            mainVm.data.getBankBalanceListByPage = getBankBalanceListByPage;

            mainVm.$watch('onReady', function(){
                $.datetimepicker.setLocale('ch');

                $('#datetime-start').datetimepicker({
                    timepicker:false,
                    format:'Y-m-d',
                    maxDate:'+1970/01/01',
                    scrollMonth:false,
                    scrollTime:false,
                    scrollInput:false,
                    onShow:function(){
                        this.setOptions({
                            maxDate: $('#datetime-end').val() ? $('#datetime-end').val() : '+1970/01/01'
                        });
                    },
                    onSelectDate: function(){
                        mainVm.data.getBankBalanceListByPage(1);
                    }
                });

                $('#datetime-end').datetimepicker({
                    timepicker:false,
                    format:'Y-m-d',
                    maxDate:'+1970/01/01',
                    scrollMonth:false,
                    scrollTime:false,
                    scrollInput:false,
                    onShow:function(){
                        this.setOptions({
                            minDate: $('#datetime-start').val() ? $('#datetime-start').val() : false
                        });
                    },
                    onSelectDate: function(){
                        mainVm.data.getBankBalanceListByPage(1);
                    }
                });

                var dateObj = new Date();
                var year = dateObj.getFullYear();
                var month = dateObj.getMonth()+1;
                var day = dateObj.getDate();

                $("#datetime-start").val(year+"-"+month+"-01");
                $("#datetime-end").val(year+"-"+month+"-"+day);

                mainVm.data.getBankBalanceListByPage(1);
            });
            avalon.scan(document.body);
        });

        avalon.router.add("/b2e/bank/front-service", function () {
            if(avalon.vmodels['main']!=undefined){
                delete avalon.vmodels['main'];
            }

            var mainVm = avalon.define({
                $id: 'main',
                template: require('../../template/b2e-bank-front-service.html'),
                data: {
                    frontServiceList: b2eModule.getFrontServiceList()
                },
                getServiceDetail: function (bankType, bankName) {
                    if(avalon.vmodels['front-service-detail-controller']!=undefined){
                        delete avalon.vmodels['front-service-detail-controller'];
                    }

                    var frontServiceDetailVm = avalon.define({
                        $id: 'front-service-detail-controller',
                        data: {
                            bankName: bankName,
                            frontServiceDetail: b2eModule.getFrontServiceDetail(bankType)
                        }
                    });

                    var frontServiceDetailTemplate = require("../../template/front-service-detail.html");

                    $('#big-modal').html(frontServiceDetailTemplate).modal({fadeDuration: 100});
                    avalon.scan(document.getElementById("big-modal").firstChild);
                },
                refreshService: function (bankType) {
                    b2eModule.getFrontServiceDetail(bankType);

                    mainVm.data.frontServiceList = b2eModule.getFrontServiceList();
                }
            });

            mainVm.$watch('onReady', function(){});
            avalon.scan(document.body);
        });

        avalon.router.add("/b2e/bank/front-service/log", function () {
            if(avalon.vmodels['main']!=undefined){
                delete avalon.vmodels['main'];
            }

            var mainVm = avalon.define({
                $id: 'main',
                template: require('../../template/b2e-bank-front-service-log.html'),
                data: {
                    bankList: b2eModule.getBankList(),
                    selectedBank: 0,
                    frontServiceLog: "",
                    selectBank: function () {
                        mainVm.data.selectedBank = document.getElementsByName("bank")[0].value;
                        mainVm.data.frontServiceLog = b2eModule.getFrontServiceLog(
                            mainVm.data.bankList[mainVm.data.selectedBank].Type,
                            $("#datetime").val()
                        ).replace(/\r\n/g, "<br />");
                    }
                }
            });

            mainVm.data.frontServiceLog = b2eModule.getFrontServiceLog(
                mainVm.data.bankList[mainVm.data.selectedBank].Type,
                $("#datetime").val()
            ).replace(/\r\n/g, "<br />");

            mainVm.$watch('onReady', function(){
                $.datetimepicker.setLocale('ch');

                $('#datetime').datetimepicker({
                    timepicker:false,
                    format:'Y-m-d',
                    maxDate:'+1970/01/01',
                    scrollMonth:false,
                    scrollTime:false,
                    scrollInput:false,
                    onSelectDate: function(){
                        mainVm.data.frontServiceLog = b2eModule.getFrontServiceLog(
                            mainVm.data.bankList[mainVm.data.selectedBank].Type,
                            $("#datetime").val()
                        ).replace(/\r\n/g, "<br />");
                    }
                });

                var dateObj = new Date();
                var year = dateObj.getFullYear();
                var month = dateObj.getMonth()+1;
                var day = dateObj.getDate();

                $("#datetime").val(year+"-"+month+"-"+day);
            });

            avalon.scan(document.body);
        });
    }
};
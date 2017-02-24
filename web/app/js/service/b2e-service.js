var commonModule = require("../module/common-module");

module.exports = {
    getBankList: function () {
        var data = [];
        $.ajax({
            url: "/b2e/bank/list",
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    getFrontServiceList: function () {
        var data = [];
        $.ajax({
            url: "/b2e/bank/front-service",
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    getFrontServiceDetail: function (bankType) {
        var data = [];
        $.ajax({
            url: "/b2e/bank/front-service/detail?bank_type="+bankType,
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data.ServiceInfo.data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    getAccountList: function (bankType) {
        var data = [];
        $.ajax({
            url: "/b2e/bank/account/list?bank_type="+bankType,
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    getBankDetailList: function (bankType, startDay, endDay, accountNo, pageNo, pageSize) {
        var url = "/b2e/bank/detail?"
            +(bankType!=undefined && bankType!=null && bankType!="" ? "bank_type="+bankType : "")
            +(startDay!=undefined && startDay!=null && startDay!="" ? "&begindate="+startDay : "")
            +(endDay!=undefined && endDay!=null && endDay!="" ? "&enddate="+endDay : "")
            +(pageNo!=undefined && pageNo!=null && pageNo!="" ? "&currentpage="+pageNo : "")
            +(accountNo!=undefined && accountNo!=null && accountNo!="" ? "&accno="+accountNo : "")
            +(pageSize!=undefined && pageSize!=null && pageSize!="" ? "&pagesize="+pageSize : "");

        var data = [];
        $.ajax({
            url: url,
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    getBankBalanceList: function (bankType, startDay, endDay, accountNo, pageNo, pageSize) {
        var url = "/b2e/bank/balance?"
            +(bankType!=undefined && bankType!=null && bankType!="" ? "bank_type="+bankType : "")
            +(startDay!=undefined && startDay!=null && startDay!="" ? "&begindate="+startDay : "")
            +(endDay!=undefined && endDay!=null && endDay!="" ? "&enddate="+endDay : "")
            +(pageNo!=undefined && pageNo!=null && pageNo!="" ? "&currentpage="+pageNo : "")
            +(accountNo!=undefined && accountNo!=null && accountNo!="" ? "&accno="+accountNo : "")
            +(pageSize!=undefined && pageSize!=null && pageSize!="" ? "&pagesize="+pageSize : "");

        var data = [];
        $.ajax({
            url: url,
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    getFrontServiceLog: function (bankType, day) {
        var data = "";
        $.ajax({
            url: "/b2e/bank/front-service/log?bank_type="+bankType+"&day="+day,
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data = response.data.Data;
                } else if (response.code == 400) {
                    commonModule.errorModal(response.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
}
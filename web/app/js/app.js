require('../scss/app.scss');

require("./router/router");

var leftMenuVm = avalon.define({
    $id: 'left-menu-controller',
    menuList: [
        {
        	label: "备付金报表管理",
            href: "",
            active: false,
            toggle: true,
			subMenuList: [
				{
					label: "中国人民银行",
					href: "#!/",
					active: true
				},
                {
                    label: "中国人民银行(New)",
                    href: "#!/pbc-new",
                    active: false
                },
				{
					label: "备付金存管银行",
					href: "#!/depository-bank",
					active: false
				},
				{
					label: "备付金合作银行",
					href: "#!/cooperative-bank",
					active: false
				}
			]
		},
		{
			label: "银企直连",
            href: "",
            active: false,
            toggle: true,
            subMenuList: [
                {
                    label: "银行明细",
                    href: "#!/b2e/bank/detail",
                    active: false
                },
                {
                    label: "银行余额",
                    href: "#!/b2e/bank/balance",
                    active: false
                },
                {
                    label: "银行前置状态",
                    href: "#!/b2e/bank/front-service",
                    active: false
                },
                {
                    label: "银行前置日志",
                    href: "#!/b2e/bank/front-service/log",
                    active: false
                }
            ]
		}
    ],
    init: function(){
        var found = false;

        for (var i = leftMenuVm.menuList.length - 1; i >= 0; i--) {
            if (leftMenuVm.menuList[i].href == window.location.hash && window.location.hash!="") {
                leftMenuVm.menuList[i].active=true;
                found = true;
            } else {
                leftMenuVm.menuList[i].active=false;

                for (var j=0; j<leftMenuVm.menuList[i].subMenuList.length; j++) {
                    if (leftMenuVm.menuList[i].subMenuList[j].href == window.location.hash) {
                        leftMenuVm.menuList[i].subMenuList[j].active = true;
                        found = true;
                    } else {
                        leftMenuVm.menuList[i].subMenuList[j].active = false;
                    }
                }
            }
        }

        if (!found) {
            if (leftMenuVm.menuList.length>0) {
                if (leftMenuVm.menuList[0].subMenuList.length>0){
                    leftMenuVm.menuList[0].subMenuList[0].active = true;
                } else {
                    leftMenuVm.menuList[0].active = true;
                }
            }
            window.location.hash = "#!/";
		}
    },
    toggle: function (index) {
        leftMenuVm.menuList[index].toggle = !leftMenuVm.menuList[index].toggle;
    },
    active: function(index, subIndex){
    	for (var i = leftMenuVm.menuList.length - 1; i >= 0; i--) {
    	    for (var j = leftMenuVm.menuList[i].subMenuList.length - 1; j >= 0; j--) {
                if (i==index && j==subIndex) {
                    leftMenuVm.menuList[i].subMenuList[j].active = true;
                } else {
                    leftMenuVm.menuList[i].subMenuList[j].active = false;
                }
            }
    	}
    }
});

leftMenuVm.init();

avalon.history.start({
    root: "/mmRouter"
});

var hash = window.location.hash.replace(/#!?/, '');
avalon.router.navigate(hash || '/', 2);

avalon.scan(document.body);
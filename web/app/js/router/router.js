var pbcRouter = require("./pbc-router");
var pbcRouterNew = require("./pbc-router-new");
var depositoryBankRouter = require("./depository-bank-router");
var cooperativeBankRouter = require("./cooperative-bank-router");
var b2eRouter = require("./b2e-router");

pbcRouter.init();
pbcRouterNew.init();
depositoryBankRouter.init();
cooperativeBankRouter.init();
b2eRouter.init();
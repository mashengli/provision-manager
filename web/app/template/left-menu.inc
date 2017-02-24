<div class="left-menu" ms-controller="left-menu-controller">
	<ul ms-visible="@menuList.length>0">
	    <div ms-for="(index, menu) in @menuList">
            <li ms-attr="{'class': menu.active ? 'active' : ''}">
                <a ms-click="@toggle(index)">
                    <img ms-if="menu.toggle" src="image/down.png">
                    <img ms-if="!menu.toggle" src="image/left.png">
                    {{menu.label}}
                </a>
            </li>

            <li ms-if="menu.toggle"
                ms-for="(subIndex, subMenu) in menu.subMenuList"
                ms-attr="{'class': subMenu.active ? 'active' : ''}">
                <a class="sub-menu"
                    ms-attr="{'href': subMenu.href}"
                    ms-click="@active(index, subIndex)">
                    {{subMenu.label}}
                </a>
            </li>
        </div>
	</ul>
</div>
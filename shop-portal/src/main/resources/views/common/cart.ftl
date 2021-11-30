<input type="hidden" id="userCartHasUserName" value="${(user.userName)!''}"/>
<div id="s_cart">
    <ul>
        <li class="nums">
            <a href="${ctx}/cart/index">购物车： <span id="s_cart_nums1">0</span> 件</a>
            <a href="" class="btn" id="s_cart_nums2"></a>
        </li>
        <li class="checkout">
            <a href="${ctx}/cart/toPreOrder">去结算&gt;&gt;</a>
        </li>
    </ul>
</div>

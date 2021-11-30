<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<#include "common/head.ftl" />
    <script type="text/javascript">
        var timeout = 500;
        var closetimer = 0;
        var ddmenuitem = 0;

        $(document).ready(function () {
            $('.cat_item').mousemove(function () {
                $(this).addClass('cat_item_on');
            });
            $('.cat_item').mouseleave(function () {
                $(this).removeClass('cat_item_on');
            });
            $('#slideshow').imgSlideShow({itemclass: 'i'})
            $("#slide-qg").switchTab({titCell: "dt a", trigger: "mouseover", delayTime: 0});
            $("#s_cart_nums1").hover(function () {
                mcancelclosetime();
                if (ddmenuitem) ddmenuitem.hide();
                ddmenuitem = $(document).find("#s_cartbox");
                ddmenuitem.fadeIn();
            }, function () {
                mclosetime();
            });
            $("#s_cart_nums2").hover(function () {
                mcancelclosetime();
                if (ddmenuitem) ddmenuitem.hide();
                ddmenuitem = $(document).find("#s_cartbox");
                ddmenuitem.fadeIn();
            }, function () {
                mclosetime();
            });
            $("#s_cartbox").hover(function () {
                mcancelclosetime();
            }, function () {
                mclosetime();
            });
            var $cur = 1;
            var $i = 4;
            var $len = $('.hot_list>ul>li').length;
            var $pages = Math.ceil($len / $i);
            var $w = $('.hotp').width() - 66;

            var $showbox = $('.hot_list');

            var $pre = $('div.left_icon');
            var $next = $('div.rgt_icon');

            $pre.click(function () {
                if (!$showbox.is(':animated')) {
                    if ($cur == 1) {
                        $showbox.animate({
                            left: '-=' + $w * ($pages - 1)
                        }, 500);
                        $cur = $pages;
                    } else {
                        $showbox.animate({
                            left: '+=' + $w
                        }, 500);
                        $cur--;
                    }

                }
            });

            $next.click(function () {
                if (!$showbox.is(':animated')) {
                    if ($cur == $pages) {
                        $showbox.animate({
                            left: 0
                        }, 500);
                        $cur = 1;
                    } else {
                        $showbox.animate({
                            left: '-=' + $w
                        }, 500);
                        $cur++;
                    }

                }
            });

        });

        function mclose() {
            if (ddmenuitem) ddmenuitem.hide();
        }

        function mclosetime() {
            closetimer = window.setTimeout(mclose, timeout);
        }

        function mcancelclosetime() {
            if (closetimer) {
                window.clearTimeout(closetimer);
                closetimer = null;
            }
        }
    </script>
</head>

<body>
<div id="doc">
    <div id="s_hdw">

        <#include "common/welcome.ftl" />

        <div class="s_hd nav">
            <div id="s_logo"><a href="#"><img src="${ctx}/images/logo.png" border="0"/></a></div>
            <div id="s_nav">
                <ul>
                    <li class="first cur"><a href="#">首页</a><span></span></li>
                    <li><a href="#">积分兑换</a><span></span></li>
                    <li><a href="#">抢购</a><span></span></li>
                    <li class="last"><a href="#">礼品</a><span></span></li>
                </ul>
            </div>
        </div><!--s_hd end-->

        <div class="mmenu">
            <div class="s_hd">
                <div id="s_search">
                    <form action="" method="post"><input name="" type="text" class="search-input"/><input name=""
                                                                                                          type="image"
                                                                                                          src="${ctx}/images/btn_search.jpg"/>
                    </form>
                </div>

                <div id="s_keyword">
                    <ul>
                        <li><strong>热门搜索：</strong></li>
                        <li><a href="#">贝玲妃</a></li>
                        <li><a href="#">SKII</a></li>
                        <li><a href="#">阿芙</a></li>
                        <li><a href="#">罗莱家纺</a></li>
                        <li><a href="#">glasslock</a></li>
                        <li><a href="#">翡翠</a></li>
                        <li><a href="#">珍珠</a></li>
                        <li><a href="#">银饰</a></li>
                        <li><a href="#">机械表</a></li>
                    </ul>
                </div>


				<#include "common/cart.ftl" />

                <div id="s_cartbox" class="s_cartbox">您的购物车中暂无商品，赶快选择心爱的商品吧！</div>

                <script type="text/javascript">
                    $(document).ready(function () {
                        $("#s_cats").hoverClass("current");
                    });
                </script>

                <div id="s_cats">
                    <div class="cat_hd"><h3><a href="#">所有商品分类</a></h3></div>
                </div>
            </div>
        </div><!--mmenu end-->

    </div><!--s_hdw end-->

    <link type="text/css" href="${ctx}/css/info.css" rel="stylesheet"/>

    <div id="s_bdw">
        <div id="s_bd">

            <div class="stepflow"><img src="${ctx}/images/step01.gif" width="980" height="32" alt=""/></div>

            <div class="addinfo">
                <a href="${ctx}/index">返回继续购物</a>
            </div>

            <div class="cartlist">
                <table width="100%">
                    <tr>
                        <th>购物车中的商品</th>
                        <th>EGO价</th>
                        <th>购买数量</th>
                        <th>是否有货</th>
                        <th>操作</th>
                    </tr>
                   <#if cartResult.cartList??>
                       <#list cartResult.cartList as cartVo>
                           <tr bgcolor="#fffaf1">
                               <td>
                                   <a href="#"><img class="smallpic" src="${(cartVo.originalImg)!""}" width="80" height="80"/></a>
                                   <a href="#">${(cartVo.goodsName)!""}</a>
                               </td>
                               <td><strong class="red">￥${(cartVo.marketPrice)!"0"}</strong></td>
                               <td>
                                   <div class="addinput">
                                       <input type="text" name="qty_item_1" value="${(cartVo.goodsNum)!"0"}" id="qty_item_1"
                                              onKeyUp="setAmount.modify('#qty_item_1')" class="stext"/>
                                       <a class="add" onClick="setAmount.add('#qty_item_1')" href="javascript:void(0)"></a>
                                       <a class="reduce" onClick="setAmount.reduce('#qty_item_1')"
                                          href="javascript:void(0)"></a>
                                   </div>
                               </td>
                               <td>有货，可当日出货</td>
                               <td><a class="delCartVo" data-id="${cartVo.goodsId}" href="javascript:void(0);" class="blue">删除</a></td>
                           </tr>
                       </#list>
                       <#else>
                          <tr>
                              暂无数据
                          </tr>
                   </#if>


                    <tr>
                        <td valign="top"><a id="clearCart" href="javascript:void(0);"><img src="${ctx}/images/deleteicon.gif"/> 清空购物车</a></td>
                        <td align="right" colspan="5">
                            <p>共${(cartResult.count)!"0"}件商品，总重3.580kg</p>
                            <p style="margin-top:10px;font-size:14px;">总计金额(不含运费)：<strong
                                        style="font-size:18px;color:#d80000;">￥${(cartResult.totalPrice)!"0"}</strong></p>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:none;padding-top:20px;" colspan="6">
                            <input type="button" value="" id="" class="btnimg f-r"/>
                            <a class="f-r goonbtn" href="#">
                                <img src="${ctx}/images/gooncat.gif" width="86" height="24" alt=""/>
                            </a>
                        </td>
                    </tr>
                </table>
            </div><!--cartlist end-->

            <script type="text/javascript">
                /* reduce_add */
                var setAmount = {
                    min: 1,
                    max: 999,
                    reg: function (x) {
                        return new RegExp("^[1-9]\\d*$").test(x);
                    },
                    amount: function (obj, mode) {
                        var x = $(obj).val();
                        if (this.reg(x)) {
                            if (mode) {
                                x++;
                            } else {
                                x--;
                            }
                        } else {
                            alert("请输入正确的数量！");
                            $(obj).val(1);
                            $(obj).focus();
                        }
                        return x;
                    },
                    reduce: function (obj) {
                        var x = this.amount(obj, false);
                        if (x >= this.min) {
                            $(obj).val(x);
                        } else {
                            alert("商品数量最少为" + this.min);
                            $(obj).val(1);
                            $(obj).focus();
                        }


                    },
                    add: function (obj) {
                        var x = this.amount(obj, true);
                        if (x <= this.max) {
                            $(obj).val(x);
                        } else {
                            alert("商品数量最多为" + this.max);
                            $(obj).val(999);
                            $(obj).focus();
                        }
                    },
                    modify: function (obj) {
                        var x = $(obj).val();
                        if (x < this.min || x > this.max || !this.reg(x)) {
                            alert("请输入正确的数量！");
                            $(obj).val(1);
                            $(obj).focus();
                        }
                    }
                }
            </script>

        </div><!--s_bd end-->
    </div><!--s_bdw end-->

    <div id="s_ftw">

        <div class="ft_quicklinks">
            <div class="ftql cf">
                <ul>
                    <li class="ftql_s">
                        <h3>购物指南</h3>
                        <ul>
                            <li><a href="">怎样购物</a></li>
                            <li><a href="">会员制</a></li>
                            <li><a href="">积分制度</a></li>
                            <li><a href="">优惠券介绍</a></li>
                            <li><a href="">订单状态说明</a></li>
                        </ul>
                    </li>
                    <li class="ftql_s">
                        <h3>服务条款</h3>
                        <ul>
                            <li><a href="">售后条款</a></li>
                            <li><a href="">退换货说明</a></li>
                            <li><a href="">联系客服</a></li>
                        </ul>
                    </li>
                    <li class="ftql_s">
                        <h3>配送方式</h3>
                        <ul>
                            <li><a href="">上门自提</a></li>
                            <li><a href="">快递运输</a></li>
                            <li><a href="">特快专递（EMS）</a></li>
                            <li><a href="">如何送礼</a></li>
                        </ul>
                    </li>
                    <li class="ftql_s">
                        <h3>支付帮助</h3>
                        <ul>
                            <li><a href="">货到付款</a></li>
                            <li><a href="">在线支付</a></li>
                            <li><a href="">邮政汇款</a></li>
                            <li><a href="">银行转账</a></li>
                            <li><a href="">发票说明</a></li>
                        </ul>
                    </li>
                    <li class="ftql_s">
                        <h3>关于EGO商城</h3>
                        <ul>
                            <li><a href="">EGO商城介绍</a></li>
                            <li><a href="">团队</a></li>
                            <li><a href="">媒体报道</a></li>
                            <li><a href="">招纳贤士</a></li>
                            <li><a href="">公告</a></li>
                        </ul>
                    </li>
                    <li class="ftql_s">
                        <div class="ftql_d">
                            <div class="str">客服中心信箱：</div>
                            <div class="val"><a href="mailto:service@shunkelong.com">sxt@bjsxt.com</a></div>
                        </div>
                        <div class="ftql_d">
                            <div class="str">客服中心热线：</div>
                            <div class="val stel">400-009-1906</div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>

        <div id="s_ft">
            <div class="ft_suggest pt100">请帮助我们提高！<a href="#">商城首页意见反馈</a></div>
            <div class="ft_banners1 tac pbt10">
                <ul>
                    <li><a href="#"><img src="${ctx}/images/ft_1.gif" border="0"/></a></li>
                    <li><a href="#"><img src="${ctx}/images/ft_2.gif" border="0"/></a></li>
                    <li><a href="#"><img src="${ctx}/images/ft_3.gif" border="0"/></a></li>
                </ul>
            </div>
            <div class="copyright tac pbt10">版权所有 Copyright&copy; EGO商城 All Rights Reserved 版权所有</div>
            <div class="ft_banners2 tac">
                <ul>
                    <li><a href="#"><img src="${ctx}/images/u255.png" border="0"/></a></li>
                    <li><a href="#"><img src="${ctx}/images/u257.png" border="0"/></a></li>
                    <li><a href="#"><img src="${ctx}/images/u259.png" border="0"/></a></li>
                    <li><a href="#"><img src="${ctx}/images/u261.png" border="0"/></a></li>
                </ul>
            </div>
        </div>

    </div><!--s_ftw end-->

</div>


<script type="text/javascript">

  $(function (){

      $("#clearCart").click(function (){
          $.ajax({
              url: "${ctx}/cart/clearCart",
              type: "POST",
              dataType: "JSON",
              success: function (result) {
                  if (200 == result.code) {
                     window.location.href=ctx+"/cart/index";
                  }
              }
          });
      })


      $(".delCartVo").click(function (){
          console.log($(this).attr("data-id"));
          var goodsId= $(this).attr("data-id");
          $.ajax({
              url: "${ctx}/cart/delCartVo",
              type: "POST",
              data:{
                  goodsId:goodsId
              },
              dataType: "JSON",
              success: function (result) {
                  if (200 == result.code) {
                      window.location.href=ctx+"/cart/index";
                  }
              }
          });
      })
  })


</script>
</body>
</html>

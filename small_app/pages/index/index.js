//index.js
var myUtils = require("../../utils/myUtils.js")

Page({
  data: {
    latitude:0,
    longitude:0,
    controls:[],
    markers:[],
  },
  /**
   * 生命周期函数--监听页面加载
   **/
   onLoad: function () {
   var that = this
    wx.getLocation({
      success:function(res){
        var longitude = res.longitude
        var latitude = res.latitude
        that.setData({
          longitude:longitude,
          latitude:latitude
        })
      }
    })

    wx.getSystemInfo({
      success: (res) => {
        var windowWidth = res.windowWidth
        var windowHeight = res.windowHeight
        that.setData({
          controls:[
            {
              id:1,
              // 扫码控件的背景图片
              iconPath: '/images/qrcode.png',
              // 扫码控件的位置
              position: {
                left: windowWidth / 2 - 33,
                top: windowHeight - 90,
                width: 66,
                height: 66
              },
              clickable: true
            },
            {
              id:2,
              // 支付控件的背景图片
              iconPath: '/images/pay.png',
              // 支付控件的位置
              position: {
                left: windowWidth - 45,
                top: windowHeight - 80,
                width: 20,
                height: 20
              },
              clickable: true
            },
            {
              id:3,
              // 报修控件的背景图片
              iconPath: '/images/fix.png',
              // 报修控件的位置
              position: {
                left: windowWidth - 45,
                top: windowHeight - 55,
                width: 20,
                height: 20
              },
              clickable: true
            },
            {
              id:4,
              // 定位控件的背景图片
              iconPath: '/images/back.png',
              // 定位控件的位置
              position: {
                left: 10,
                top: windowHeight - 30,
                width: 20,
                height: 20
              },
              clickable: true
            },
            {
              id:5,
              // 大头针控件的背景图片
              iconPath: '/images/location.png',
              // 大头针控件的位置
              position: {
                left: windowWidth /  2,
                top: windowHeight / 2,
                width: 35,
                height: 35
              },
              clickable: true
            },
            {
              id:6,
              // 添加控件的背景图片
              iconPath: '/images/add.png',
              // 添加控件的位置
              position: {
                width: 25,
                height: 25
              },
              clickable: true
            },
          ]
        })
      },
    })
  },

  /**
   * 控件被点击的事件
   */
  controltap: function(e) {
    var cid = e.controlId
    var that = this
    switch(cid) {
      //扫码用车
      case 1: {
        var status = myUtils.get("status")
        if (status == 0) {
          wx.navigateTo({
            url: '../register/register',
          })
        } else if(status == 1) {
          wx.navigateTo({
            url: '../deposit/deposit',
          })
        } else if (status == 2) {
          wx.navigateTo({
            url: '../identify/identify',
          }) 
        } else {
          // 调用扫码功能
          myUtils.scanCode()
        }
        break
      }
      // 回到原点
      case 4: {
        that.mapctx.moveToLocation()
        break
      }
      // 添加单车
      case 6: {
        this.mapctx.getCenterLocation({
          success:function(res){
            var longitude = res.longitude
            var latitude = res.latitude
            wx.request({
              url: 'http://127.0.0.1:8080/bike/add',
              data:{
                location:[longitude, latitude],
                status: 0
              },
              method:'POST',
              success:function(res){
                findNearBikes(that, longitude, latitude)
              }
            }) 
          }
        })
        break
      }
    }
  },

  /**
   * 移动后地图地图视野发生变化触发的事件
   */
  regionchange: function(e) {
    console.log(e)
    var that = this
    //获取移动后的位置
    var etype = e.type
    if (etype == 'end') {
      this.mapctx.getCenterLocation({
        success:function(res) {
          var longitude = res.longitude
          var latitude = res.latitude 
          findNearBikes(that, longitude, latitude)
        }
      })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    //创建map上下文
    this.mapctx = wx.createMapContext('myMap')
  },
})

function findNearBikes(that, longitude, latitude) {
  wx.request({
    url: 'http://127.0.0.1:8080/bike/findNear',
    method: 'GET',
    data: {
      longitude: longitude,
      latitude: latitude
    },
    success: function(res) {
      console.log(res)
      var bikes = res.data.map((geoResult) => {
        return {
           iconPath:"/images/bike.png",
            width: 35,
            height: 30,
            longitude: geoResult.content.location[0],
            latitude: geoResult.content.location[1],
            id: geoResult.content.id
        }
      })
      // 重新赋值
      that.setData({
        markers:bikes
      })
    }
  })
}

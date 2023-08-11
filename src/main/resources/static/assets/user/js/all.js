var app = angular.module("myApp", []);

app.controller("myCtrl", function ($scope, $http, $timeout) {
  $scope.inputQuantities = {};
  $scope.isLoading = true;
  $scope.show = true;
  $scope.quantityDetail = 1;
  $timeout(function () {
    $scope.isLoading = false;
  }, 1500);
  var totalAmountElement = document.getElementById("tongSoSanPham").innerHTML;
  if (totalAmountElement > 0) {
    $scope.show = false;
  }
  function formatCurrency(amount) {
    var formattedAmount = amount.toLocaleString("vi-VN");
    return formattedAmount.replace(/\./g, ",");
  }
  $scope.loadGioHang = function (mauSacId) {
    $http
      .get("/giohanginfo?colorId=" + mauSacId)
      .then(function (response) {
        var tongSoLuong = response.data.tongSoSanPham;
        var tongTien = response.data.tongTien;
        var totalAmountElement = document.getElementById("tongSoSanPham");
        if (totalAmountElement) {
          totalAmountElement.textContent = tongSoLuong;
        }
        var totalAmountElements = document.getElementsByClassName("tongTien");
        for (var i = 0; i < totalAmountElements.length; i++) {
          totalAmountElements[i].textContent = formatCurrency(tongTien) + " ₫";
        }
        if (tongSoLuong == 0) {
          $scope.show = true;
        }
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };

  $scope.findColorId = function (mauSacId) {
    $http
      .get("/findColorId?colorId=" + mauSacId)
      .then(function (response) {
        var rowToRemove = document.querySelector(
          '[product-subtotal="' + mauSacId + '"]'
        );
        $scope.inputQuantities[mauSacId] = response.data.soLuong;
        rowToRemove.textContent = formatCurrency(response.data.tongGia) + " ₫";
        $scope.loadGioHang();
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };
  $scope.addToCart = function (colorId, quantity) {
    $http
      .post("/add?colorId=" + colorId + "&quantity=" + quantity)
      .then(function (response) {
        if (response.data.message.includes("thành công")) {
          createToast("success", "Thêm vào giỏ hàng thành công!");
        } else {
          createToast("error", "Sản phẩm đã hết hàng!");
        }
        $scope.loadGioHang();
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };
  $scope.updateCart = function (mauSacId) {
    var colorId = mauSacId;
    var quantity = $scope.inputQuantities[mauSacId];
    if (quantity > 0) {
      $http
        .post("/update?colorId=" + colorId + "&quantity=" + quantity)
        .then(function (response) {
          $scope.findColorId(colorId);
        })
        .catch(function (error) {
          console.error("Error fetching data:", error);
        });
    } else if (quantity != "" && quantity <= 0) {
      $scope.removeItem(colorId);
    }
  };
  $scope.updateDI = function (mauSacId, quantity) {
    var colorId = mauSacId;
    var quantity = quantity;
    if (quantity > 0) {
      $http
        .post("/update?colorId=" + colorId + "&quantity=" + quantity)
        .then(function (response) {
          $scope.findColorId(colorId);
        })
        .catch(function (error) {
          console.error("Error fetching data:", error);
        });
    } else if (quantity != "" && quantity < 0) {
      $scope.removeItem(colorId);
    }
  };
  $scope.deleteCart = function (colorId) {
    $http
      .post("/delete?colorId=" + colorId)
      .then(function (response) {
        $scope.loadGioHang();
      })
      .catch(function (error) {
        console.error("Error fetching data:", error);
      });
  };
  $scope.removeItem = function (mauSacId) {
    var rowToRemove = document.querySelector(
      '[data-product-id="' + mauSacId + '"]'
    );
    if (rowToRemove) {
      $scope.deleteCart(mauSacId);
      rowToRemove.remove();
    }
  };
  $scope.decreaseQuantity = function (mauSacId) {
    $scope.updateDI(mauSacId, $scope.inputQuantities[mauSacId] - 1);
  };
  $scope.increaseQuantity = function (mauSacId) {
    $scope.updateDI(mauSacId, $scope.inputQuantities[mauSacId] + 1);
    console.log("Hello");
  };
  $(document).on("click", ".nice-select .option:not(.disabled)", function (t) {
    var s = $(this),
      n = s.closest(".nice-select");
    $("#detailAdd").attr("data-color-id", s.data("value"));
    $(".sm-image[data-mau-sac-id='" + s.data("value") + "']").click();
  });
  $scope.decreaseDetailQuantity = function () {
    if ($scope.quantityDetail > 1) {
      $scope.quantityDetail--;
    }
  };
  $scope.increaseDetailQuantity = function () {
    $scope.quantityDetail++;
  };
  $scope.addDetailCart = function () {
    var colorId = $("#detailAdd").attr("data-color-id");
    var quantity = $scope.quantityDetail;
    $scope.addToCart(colorId, quantity);
    console.log("Hello")
  }
});
function changeSortBy(value) {
  var url = window.location.href;
  var separator = url.indexOf("?") !== -1 ? "&" : "?";
  if (url.includes("sortBy=")) {
    url = url.replace(/(sortBy=)[^\&]+/, "$1" + value);
  } else {
    url = url + separator + "sortBy=" + value;
  }
  window.location.href = url;
}
$("#checkoutBtn").on("click", function (event) {
  event.preventDefault();
  var selectedPaymentMethod = $("#selectedPaymentMethod").val();
  var formAction = $("form").attr("action"); // Lấy giá trị hiện tại của thuộc tính action
  formAction =
    "/user/checkout?phuongThucThanhToanId=" + selectedPaymentMethod; // Cộng thêm phuongThucThanhToanId vào action
  $("form").attr("action", formAction);
  $("#paymentNow").click(); // Nhấn nút "paymentNow"
});


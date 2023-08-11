app.factory("ShippingService", [
  "$http",
  function ($http) {
    var baseUrl = "https://online-gateway.ghn.vn/shiip/public-api";
    var token = "4525b920-35a7-11ee-a59f-a260851ba65c"; // Thay thế YOUR_API_TOKEN bằng token thực tế của bạn

    return {
      // Hàm gọi API lấy danh sách tỉnh
      getProvinces: function () {
        var config = {
          headers: { token: token },
        };
        return $http.get(baseUrl + "/master-data/province", config);
      },
      // Hàm gọi API lấy danh sách quận/huyện dựa trên ID tỉnh đã chọn
      getDistricts: function (provinceId) {
        var config = {
          headers: { token: token },
        };
        var data = { province_id: provinceId };
        return $http.post(baseUrl + "/master-data/district", data, config);
      },
      // Hàm gọi API lấy danh sách xã dựa trên ID quận/huyện đã chọn
      getWards: function (districtId) {
        var config = {
          headers: { token: token },
        };
        var data = { district_id: districtId };
        return $http.post(baseUrl + "/master-data/ward", data, config);
      },
    };
  },
]);

app.controller("info", [
  "$scope",
  "ShippingService",
  function ($scope, ShippingService) {
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.selectedProvince = null;
    $scope.selectedDistrict = null;
    $scope.selectedWard = null;
    $scope.selectedProvinceId = null;
    $scope.selectedDistrictId = null;
    $scope.selectedWardId = null;
    $scope.findProvinceById = function (ProvinceName) {
      for (var i = 0; i < $scope.provinces.length; i++) {
        if ($scope.provinces[i].ProvinceName === ProvinceName) {
          $scope.selectedProvinceId = $scope.provinces[i].ProvinceID;
          break;
        }
      }
    };
    $scope.findDistrictById = function (DistrictName) {
      for (var i = 0; i < $scope.districts.length; i++) {
        if ($scope.districts[i].DistrictName === DistrictName) {
          $scope.selectedDistrictId = $scope.districts[i].DistrictID;
          break;
        }
      }
    };

    ShippingService.getProvinces().then(function (response) {
      $scope.provinces = response.data.data;
      var selectedProvinceValue = $("#tinhThanh").attr("data-province");
      var selectedDistrictValue = $("#quanHuyen").attr("data-district");
      var selectedWardValue = $("#xaPhuong").attr("data-ward");
      $scope.findProvinceById(selectedProvinceValue);
      $scope.findDistrictById(selectedDistrictValue);
      $scope.selectedProvince = selectedProvinceValue;
      if (selectedDistrictValue) {
        if ($scope.selectedProvinceId) {
          ShippingService.getDistricts($scope.selectedProvinceId).then(
            function (response) {
              $scope.districts = response.data.data;
              $scope.selectedDistrict = selectedDistrictValue;
              $scope.findDistrictById(selectedDistrictValue);
              if (selectedWardValue) {
                ShippingService.getWards($scope.selectedDistrictId).then(
                  function (response) {
                    $scope.wards = response.data.data;
                    $scope.selectedWard = selectedWardValue;
                  }
                );
              }
            }
          );
        }
      }
    });

    $scope.getDistricts = function () {
      console.log($scope.selectedProvince);
      if ($scope.selectedProvince) {
        $scope.findProvinceById($scope.selectedProvince);
        ShippingService.getDistricts($scope.selectedProvinceId).then(function (
          response
        ) {
          $scope.districts = response.data.data;
          $scope.selectedDistrict = null;
          $scope.selectedWard = null;
        });
      } else {
        $scope.districts = [];
        $scope.wards = [];
        $scope.selectedDistrict = null;
        $scope.selectedWard = null;
        $scope.shippingFee = 0;
      }
    };

    $scope.getWards = function () {
      if ($scope.selectedDistrict) {
        $scope.findDistrictById($scope.selectedDistrict);
        console.log($scope.selectedDistrictId);
        ShippingService.getWards($scope.selectedDistrictId).then(function (
          response
        ) {
          $scope.wards = response.data.data;
          $scope.selectedWard = null;
          $scope.shippingFee = 0;
        });
      } else {
        $scope.wards = [];
        $scope.selectedWard = null;
        $scope.shippingFee = 0;
      }
    };
  },
]);

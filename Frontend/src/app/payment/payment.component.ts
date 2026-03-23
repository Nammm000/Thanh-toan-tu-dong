import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { PlanService } from 'src/app/service/plan.service';
import { NewsService } from 'src/app/service/news.service';
import { PaymentService } from 'src/app/service/payment.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { SnackbarService } from 'src/app/service/snackbar.service';
import { ActivatedRoute, Router } from '@angular/router';
// import { QRCode } from 'antd';
import * as crypto from 'crypto';
import { MoMoRequest } from '../model/MoMoRequest.model';
// import { time } from 'console';
declare var google: any;

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent {
  @ViewChild('addressInput') addressInput!: ElementRef;

  showQRCode: boolean = false;
  responseMessage: any;
  type: string = '';
  phoneNumber: string = '';
  address: string = '';
  orderNote: string = '';
  product = {
    name: 'Sản phẩm mẫu',
    price: 50000,
    description: 'Mô tả sản phẩm mẫu',
    duration: '0',
  };

  data = {
    price: this.product.price,
    orderInfo: this.product.description,
    // redirectUrl: "http://localhost:4200/payment-return",
    // ipnUrl: "http://localhost:8080/api/payment/momo/ipn",
  };
  result = {
    payUrl: '',
    qrUrl: '',
    url: '',
  };

  options = [
    { label: 'Momo', value: 'Momo', image: 'assets/payment/momo.png' },
    { label: 'VNPAY', value: 'VNPAY', image: 'assets/payment/vnpay.png' },
    { label: 'ZaloPay', value: 'ZaloPay', image: 'assets/payment/zalopay.png' },
  ];
  selectedPaymentOption: string = '';

  constructor(
    private planService: PlanService,
    private newsService: NewsService,
    private paymentService: PaymentService,
    // private sanitizer: DomSanitizer,
    private ngxService: NgxUiLoaderService,
    private snackbarService: SnackbarService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    // console.log(this.activatedRoute.snapshot.url); // UrlSegment[]
    // console.log(this.activatedRoute.snapshot.url[0]); // UrlSegment
    this.type = this.activatedRoute.snapshot.url[0].path;
    if (this.type === 'payment') {
      this.getPlanById(id);
    } else if (this.type === 'news') {
      this.getNewsById(id);
    }
  }

  ngAfterViewInit() {
    const autocomplete = new google.maps.places.Autocomplete(
      this.addressInput.nativeElement,
      // , {
      //   types: ['geocode'], // Chỉ giới hạn loại địa điểm là địa chỉ
      //   componentRestrictions: { country: 'vn' }, // Giới hạn kết quả chỉ trong Việt Nam
      // }
    );

    autocomplete.addListener('place_changed', () => {
      const place = autocomplete.getPlace();
      console.log(place.formatted_address);
      // console.log(place.geometry.location.lat());
      // console.log(place.geometry.location.lng());
    });
  }

  getPayment() {
    this.data.price = this.product.price;
    this.data.orderInfo = this.product.description + ' - ' + this.product.name;

    this.ngxService.start();

    if (this.selectedPaymentOption === 'Momo') {
      this.paymentService.createMomo(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          // console.log(response);
          if (response.resultCode === 0) {
            this.result.url = response.deeplink;
            this.result.qrUrl = response.qrCodeUrl;
            this.showQRCode = true;
          }
          
          // window.location.href = this.result.url;
        },
        (error: any) => {
          this.ngxService.stop();
          if (error.error?.message) {
            this.responseMessage = error.error?.message;
          } else {
            this.responseMessage = GlobalConstants.genericError;
          }
          this.snackbarService.openSnackBar(
            this.responseMessage,
            GlobalConstants.error,
          );
        },
      );
    } else if (this.selectedPaymentOption === 'VNPAY') {
      this.paymentService.createVNPay(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          // console.log(response);
          this.result.url = response.payUrl;
          // window.location.href = this.result.url;
        },
        (error: any) => {
          this.ngxService.stop();
          if (error.error?.message) {
            this.responseMessage = error.error?.message;
          } else {
            this.responseMessage = GlobalConstants.genericError;
          }
          this.snackbarService.openSnackBar(
            this.responseMessage,
            GlobalConstants.error,
          );
        },
      );
    } else if (this.selectedPaymentOption === 'ZaloPay') {
      this.paymentService.createZaloPay(this.data).subscribe(
        (response: any) => {
          this.ngxService.stop();
          // console.log(response);
          if (response.return_code === 1) {
            this.result.url = response.orderUrl;
            // this.result.qrUrl = response.qrCodeUrl;
            this.showQRCode = true;
          }
          // window.location.href = this.result.url;
        },
        (error: any) => {
          this.ngxService.stop();
          if (error.error?.message) {
            this.responseMessage = error.error?.message;
          } else {
            this.responseMessage = GlobalConstants.genericError;
          }
          this.snackbarService.openSnackBar(
            this.responseMessage,
            GlobalConstants.error,
          );
        },
      );
    }
  }

  getNewsById(id: any) {
    this.newsService.getNewsById(id).subscribe(
      (response: any) => {
        // this.data = response;
        console.log(response);
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(
          this.responseMessage,
          GlobalConstants.error,
        );
      },
    );
  }

  getPlanById(id: any) {
    this.planService.getPlanById(id).subscribe(
      (response: any) => {
        // this.data = response;
        // console.log(response);
        this.product.name = response.name;
        this.product.price = response.price;
        this.product.description = response.description;
        this.product.duration = response.duration;
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(
          this.responseMessage,
          GlobalConstants.error,
        );
      },
    );
  }

  // constructor() {
  //   //https://developers.momo.vn/#/docs/en/aiov2/?id=payment-method
  //   //parameters
  //   const partnerCode = "MOMO";
  //   const accessKey = "F8BBA842ECF85";
  //   const secretkey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
  //   const requestId = partnerCode + new Date().getTime();
  //   const orderId = requestId;
  //   const orderInfo = "pay with MoMo";
  //   const redirectUrl = "https://momo.vn/return";
  //   const ipnUrl = "https://callback.url/notify";
  //   // const ipnUrl = redirectUrl = "https://webhook.site/454e7b77-f177-4ece-8236-ddf1c26ba7f8";
  //   const amount = "50000";
  //   const requestType = "captureWallet"
  //   const extraData = ""; //pass empty value if your merchant does not have stores

  //   //before sign HMAC SHA256 with format
  //   //accessKey=$accessKey&amount=$amount&extraData=$extraData&ipnUrl=$ipnUrl&orderId=$orderId&orderInfo=$orderInfo&partnerCode=$partnerCode&redirectUrl=$redirectUrl&requestId=$requestId&requestType=$requestType
  //   const rawSignature = "accessKey="+accessKey+"&amount=" + amount+"&extraData=" + extraData+"&ipnUrl=" + ipnUrl+"&orderId=" + orderId+"&orderInfo=" + orderInfo+"&partnerCode=" + partnerCode +"&redirectUrl=" + redirectUrl+"&requestId=" + requestId+"&requestType=" + requestType
  //   //puts raw signature
  //   console.log("--------------------RAW SIGNATURE----------------")
  //   console.log(rawSignature)
  //   //signature
  //   const crypto = require('crypto');
  //   const signature = crypto.createHmac('sha256', secretkey)
  //       .update(rawSignature)
  //       .digest('hex');
  //   console.log("--------------------SIGNATURE----------------")
  //   console.log(signature)

  //   //json object send to MoMo endpoint
  //   const requestBody = JSON.stringify({
  //       partnerCode : partnerCode,
  //       accessKey : accessKey,
  //       requestId : requestId,
  //       amount : amount,
  //       orderId : orderId,
  //       orderInfo : orderInfo,
  //       redirectUrl : redirectUrl,
  //       ipnUrl : ipnUrl,
  //       extraData : extraData,
  //       requestType : requestType,
  //       signature : signature,
  //       lang: 'en'
  //   });
  // }
}

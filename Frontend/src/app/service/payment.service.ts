import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  url = environment.apiUrl + "/api/payment";
  constructor(private httpClient: HttpClient) { }

  createPayment(data: any, type: string) {
    let endpoint = "";
    switch (type) {
      case "Momo":
        endpoint = "/createMomo";
        break;
      case "VNPAY":
        endpoint = "/createVNPay";
        break;
      case "ZaloPay":
        endpoint = "/createZaloPay";
        break;
      default:
        throw new Error("Unsupported payment type");
    }
    return this.httpClient.post(this.url + endpoint, data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  createMomo(data: any) {
    return this.httpClient.post(this.url + "/createMomo", data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  handleIpn(data: any) {
    return this.httpClient.post(this.url + "/momo/ipn", data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  createVNPay(data: any) {
    return this.httpClient.post(this.url + "/createVNPay", data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  paymentReturn(data: any) {
    return this.httpClient.post(this.url + "/vnpay-return", data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  createZaloPay(data: any) {
    return this.httpClient.post(this.url + "/createZaloPay", data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  callback(data: any) {
    return this.httpClient.post(this.url + "/callback", data, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }
}

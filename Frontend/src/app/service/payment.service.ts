import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  url = environment.apiUrl + "/api/payment";
  constructor(private httpClient: HttpClient) { }

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

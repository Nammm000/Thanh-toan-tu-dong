import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

export interface PaymentRequestDTO {
  amount: number;
  orderInfo: string;
  method: 'VNPAY' | 'MOMO' | 'ZALOPAY';
}

export interface PaymentResponseDTO {
  paymentUrl: string;
  qrCode: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  url = environment.apiUrl + "/api/payment";
  constructor(private httpClient: HttpClient) { }

  /**
   * Create a payment using the new unified endpoint
   * @param request PaymentRequestDTO containing amount, orderInfo, and payment method
   * @returns Observable<PaymentResponseDTO> with paymentUrl and qrCode
   */
  createPaymentUnified(request: PaymentRequestDTO) {
    return this.httpClient.post<PaymentResponseDTO>(this.url + "/create", request, {
       headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  /**
   * Legacy method - Create payment using individual endpoints per payment method
   * @deprecated Use createPaymentUnified instead
   */
  createPayment(data: any, type: string) {
    let endpoint = "";
    switch (type) {
      case "MOMO":
        endpoint = "/createMomo";
        break;
      case "VNPAY":
        endpoint = "/createVNPay";
        break;
      case "ZALOPAY":
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

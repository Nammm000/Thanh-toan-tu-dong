import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  add(data: any) {
    return this.httpClient.post(this.url + "/customer/add", data, {
       headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  update(data: any) {
    return this.httpClient.post(this.url + "/customer/update", data,  {
       headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  getCustomers() {
    return this.httpClient.get(this.url + "/customer/get");
  }

  updateStatus(data: any) {
    return this.httpClient.post(this.url + "/customer/updateStatus", data,  {
       headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + "/customer/delete/" + id, {
      headers: new HttpHeaders().set('Content-Type', "application/json")
   })
  }

  getCustomerById(id: any) {
    return this.httpClient.get(this.url + "/customer/getCustomerById/" + id);
  }

  getContactNumbers() {
    return this.httpClient.get(this.url + "/customer/getContactNumbers?filter=true");
  }

}

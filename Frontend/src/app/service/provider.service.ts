import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProviderService {

  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  add(data: any) {
    return this.httpClient.post(this.url + "/provider/add", data, {
       headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  update(data: any) {
    return this.httpClient.post(this.url + "/provider/update", data,  {
       headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  getProviders() {
    return this.httpClient.get(this.url + "/provider/get");
  }

  updateStatus(data: any) {
    return this.httpClient.post(this.url + "/provider/updateStatus", data,  {
       headers: new HttpHeaders().set('Content-Type', "application/json")
    })
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + "/provider/delete/" + id, {
      headers: new HttpHeaders().set('Content-Type', "application/json")
   })
  }

  getProviderById(id: any) {
    return this.httpClient.get(this.url + "/provider/getProviderById/" + id);
  }

}

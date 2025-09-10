import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  add(data: any) {
      return this.httpClient.post(this.url + "/location/add", data, {
        headers: new HttpHeaders().set('content-Type', "application/json")
      })
  }

  update(data: any) {
    return this.httpClient.post(this.url + "/location/update", data, {
      headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + "/location/delete/" + id, {
      headers: new HttpHeaders().set('content-Type', "application/json")
   })
  }

  getLocations() {
    return this.httpClient.get(this.url + "/location/get");
  }

  getFilteredLocations() {
    return this.httpClient.get(this.url + "/location/get?filter=true");
  }
}

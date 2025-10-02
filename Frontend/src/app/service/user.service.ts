import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  signUp(data: any): Observable<any> {
    return this.httpClient.post(this.url + "/auth/sign-up", data);
  }

  forgotPassword(data: any): Observable<any> {
    return this.httpClient.get("http://localhost:8082/auth/hello");
    // return this.httpClient.post(this.url
    //   + "/auth/forgotPassword", data, {
    //   headers: new HttpHeaders().set('Content-Type', 'application/json')
    // });
  }

  login(data: any): Observable<any> {
    return this.httpClient.post(this.url + "/auth/login", data);
  }

  logout(): Observable<any> {
    return this.httpClient.post(this.url + "/api/logout", {}, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
  });
  }

  // checkToken() {
  //   return this.httpClient.get(this.url + "/user/checkToken");
  // }

  changePassword(data: any) {
    return this.httpClient.post(this.url
      + "/api/changePassword", data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  getUser() {
    return this.httpClient.get(this.url + "/user/get");
  }

  updateUser(data: any) {
    return this.httpClient.post(this.url
      + "/user/update", data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    })
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + "/user/delete/" + id, {
      headers: new HttpHeaders().set('content-Type', "application/json")
   })
  }
}

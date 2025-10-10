import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PlanService {

  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) { }

  add(data: any) {
      return this.httpClient.post(this.url + "/plan/add", data, {
        headers: new HttpHeaders().set('content-Type', "application/json")
      })
  }

  update(data: any) {
    return this.httpClient.post(this.url + "/plan/update", data, {
      headers: new HttpHeaders().set('content-Type', "application/json")
    })
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + "/plan/delete/" + id, {
      headers: new HttpHeaders().set('content-Type', "application/json")
   })
  }

  getPlans() {
    return this.httpClient.get(this.url + "/plan/get");
  }

  getAllPlanCode(): Observable<any> {
    return this.httpClient.get(this.url + "/plan/getAllPlanCode");
  }

  getAllPlanCodeDescription(): Observable<any> {
    return this.httpClient.get(this.url + "/plan/getAllPlanCodeDescription");
  }

  getPlanById(id: any): Observable<any> {
    return this.httpClient.get(this.url + "/plan/getPlanById/" + id);
  }

  addNew(data: FormData): Observable<any> {
      return this.httpClient.post(this.url + "/imagePaymentController/addNew", data);
  }

  getPayments() {
    return this.httpClient.get(this.url + "/imagePaymentController/get");
  }

  deletePayments(id: any) {
    return this.httpClient.delete(this.url + "/imagePaymentController/delete/" + id, {
      headers: new HttpHeaders().set('content-Type', "application/json")
   })
  }

  // getFilteredCategorys() {
  //   return this.httpClient.get(this.url + "/plan/get?filter=true");
  // }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Dashboard } from '../model/dashboard';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  url = environment.apiUrl;

  constructor(private httpClient: HttpClient) { }

  getDetails(): Observable<Dashboard> {
    return this.httpClient.get<Dashboard>(this.url + "/dashboard/details");
  }
}

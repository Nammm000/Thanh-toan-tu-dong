import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NewsService {
  url = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}

  add(data: any) {
    return this.httpClient.post(this.url + '/news/add', data, {
      headers: new HttpHeaders().set('content-Type', 'application/json'),
    });
  }

  update(data: any) {
    return this.httpClient.post(this.url + '/news/update', data, {
      headers: new HttpHeaders().set('content-Type', 'application/json'),
    });
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + '/news/delete/' + id, {
      headers: new HttpHeaders().set('content-Type', 'application/json'),
    });
  }

  getNews() {
    return this.httpClient.get(this.url + '/news/get');
  }

  getPublicNews() {
    return this.httpClient.get(this.url + '/news/getPublicNews');
  }

  getUserSubNews() {
    return this.httpClient.get(this.url + '/news/getUserSubNews');
  }

  getNewsById(id: any): Observable<any> {
    return this.httpClient.get(this.url + '/news/getNewsById/' + id);
  }

  // getFilteredCategorys() {
  //   return this.httpClient.get(this.url + "/news/get?filter=true");
  // }
}

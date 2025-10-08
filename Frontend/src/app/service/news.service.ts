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

  add(data: FormData) {
    return this.httpClient.post(this.url + '/news/add', data);
  }

  update(data: FormData) {
    return this.httpClient.post(this.url + '/news/update', data);
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

  getUserSub(): Observable<any> {
    return this.httpClient.get(this.url + '/news/getUserSub');
  }

  getAllNews(): Observable<any> {
    return this.httpClient.get(this.url + '/news/getAllNews');
  }

  updateViews(id: any) {
    return this.httpClient.get(this.url + '/news/updateViews/' + id);
  }

  // getFilteredCategorys() {
  //   return this.httpClient.get(this.url + "/news/get?filter=true");
  // }
}

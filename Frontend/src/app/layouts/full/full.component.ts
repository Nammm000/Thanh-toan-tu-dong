import { MediaMatcher } from '@angular/cdk/layout';
import { Router } from '@angular/router';
import { ChangeDetectorRef, Component, OnDestroy, AfterViewInit } from '@angular/core';


/** @title Responsive sidenav */
@Component({
  selector: 'app-full-layout',
  templateUrl: 'full.component.html',
  styleUrls: []
})
export class FullComponent implements OnDestroy, AfterViewInit {
  mobileQuery: MediaQueryList;
  tab = '';
  isLeftSidebarCollapsed = false;

  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher,
    private router: Router
  ) {
    this.mobileQuery = media.matchMedia('(min-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit(): void {
    this.router.url.endsWith('/admin');
    this.router.url.endsWith('/admin/users');
    this.router.url.endsWith('/admin/news');
    this.router.url.endsWith('/admin/contacts');
    this.router.url.endsWith('/admin/plans');
    this.router.url.endsWith('/admin/subscriptions');
    this.router.url.endsWith('/admin/payments');
    this.router.url.endsWith('/admin/settings');
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
  ngAfterViewInit() { }

  changeIsLeftSidebarCollapsed(boo: boolean): void {
    this.isLeftSidebarCollapsed = boo;
  }
}
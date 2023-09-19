import { Directive, ElementRef, EventEmitter, HostListener, Output } from '@angular/core';

@Directive({
  selector: '[recScrollTracker]'
})
export class ScrollTrackerDirective {

  @Output() scrolledUp: EventEmitter<number> = new EventEmitter<number>();
  @Output() scrolledDown: EventEmitter<number> = new EventEmitter<number>();

  private previousScrollTop: number = 0;
  private isScrolledDown: boolean = false;
  private isScrolledUp: boolean = false;
  private scrollUpBreakpoint: number = 30;
  private scrollDownBreakpoint: number = 90;

  constructor(
    private el: ElementRef
  ) { }

  @HostListener('scroll', ['$event'])
  public onScroll(): void {
    const scrollContainer: HTMLElement = this.el.nativeElement as HTMLElement;
    const scrollTop: number = scrollContainer.scrollTop;
    const scrollHeight: number = scrollContainer.scrollHeight;
    const clientHeight: number = scrollContainer.clientHeight;
    const scrollPercentage: number = (scrollTop / (scrollHeight - clientHeight)) * 100;

    if (this.isScrolledDown && scrollPercentage < this.scrollDownBreakpoint) {
      this.isScrolledDown = false;
    }

    if (scrollTop < this.previousScrollTop && scrollPercentage <= this.scrollUpBreakpoint && !this.isScrolledUp) {
      if (scrollPercentage < this.scrollDownBreakpoint) {
        this.isScrolledDown = false;
      }

      this.isScrolledUp = true;
      this.scrolledUp.emit(scrollPercentage);
    } else if (scrollTop > this.previousScrollTop && scrollPercentage >= this.scrollDownBreakpoint && !this.isScrolledDown) {
      if (scrollPercentage > this.scrollDownBreakpoint) {
        this.isScrolledUp = false;
      }

      this.isScrolledDown = true;
      this.scrolledDown.emit(scrollPercentage);
    }

    this.previousScrollTop = scrollTop;
  }

}

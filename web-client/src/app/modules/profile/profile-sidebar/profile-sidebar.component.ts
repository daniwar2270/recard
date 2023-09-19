import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProfileService } from '../../../core/service/profile/profile.service';
import { StatusMessageService } from '../../../core/service/modal/status-message.service';
import { ERROR, ImageUpload, SUCCESS } from '../../shared/constants/pop-up.constants';
import { ProfileNavigation } from '../../shared/constants/profile.constants';
import { MessageType } from '../../../core/models/modals/success-message.types';

@Component({
  selector: 'rec-profile-sidebar',
  templateUrl: './profile-sidebar.component.html',
  styleUrls: ['./profile-sidebar.component.scss']
})
export class ProfileSidebarComponent {

  protected readonly ProfileNavigation: any = ProfileNavigation;

  @Input() profileImage: string = '';

  @Output() selectSectionEvent: EventEmitter<string> = new EventEmitter<string>();

  public selectedSection: string = ProfileNavigation.ACCOUNT;
  public tempImage: string | null = null;

  private file: File | null = null;

  constructor(
    private profileService: ProfileService,
    private messageService: StatusMessageService
  ) { }

  public onFileChange(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader: FileReader = new FileReader();
      reader.onload = () => this.tempImage = reader.result as string;
      reader.readAsDataURL(file);

      this.file = file;
    }
    event.target.value = null;
  }

  public uploadImage(): void {
    if (this.tempImage && this.file) {
      this.profileImage = this.tempImage;

      this.profileService.uploadImage(this.file).subscribe({
        next: (): void => {
          this.messageService.initiate({
            title: SUCCESS,
            content: ImageUpload.UPLOAD_SUCCESS,
            type: MessageType.success
          });
        },
        error: (error: Error): void => {
          this.messageService.initiate({
            title: ERROR,
            content: error.message,
            type: MessageType.error
          });
        }
      });
      this.file = null;
      this.tempImage = null;
    }
  }

  public cancelImage(): void {
    this.tempImage = null;
  }

  public selectSection(section: string): void {
    this.selectedSection = section;
    this.selectSectionEvent.emit(this.selectedSection);
  }

}

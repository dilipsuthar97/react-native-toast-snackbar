#import "MyLibraryView.h"
#import "MyLibrary.h"

@implementation MyLibrary

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(showToast:(NSDictionary *)options) {
    [MyLibraryView showToastWithOptions:options]
}
@end

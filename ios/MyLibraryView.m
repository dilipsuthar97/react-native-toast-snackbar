//
//  MyLibraryView.m
//  MyLibrary
//
//  Created by C100-135 on 11/05/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "MyLibraryView.h"
#import <React/RCTConvert.h>

#define SCREEN_HEIGHT [[UIScreen mainScreen] bounds].size.height
#define SCREEN_WIDTH [[UIScreen mainScreen] bounds].size.width

@interface MyLibraryView

+ (void)showToastWithOptions:(NSDictionary *)options {
    UILabel *lblMessage = [[UILabel alloc] init];
    lblMessage.textAlignment = NSTextAlignmentCenter;
    lblMessage.text = @"Messagaeee";
    lblMessage.font = [UIFont systemFontOfSize:12.0];
    lblMessage.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5f];
    
    CGSize textSize = [[lblMessage text] sizeWithAttributes:@{NSFontAttributeName:[lblMessage font]}];
}

@end

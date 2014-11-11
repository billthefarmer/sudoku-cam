/**
 * @file windefs.h
 * Copyright 2012, 2013 MinGW.org project
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice (including
 * the next paragraph) shall be included in all copies or substantial
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

#ifndef _WINDEFS_H
#define _WINDEFS_H

// Types and definitions from windows include files taken from the
// MinGW project

#define GetRValue(c) ((BYTE)(c))
#define GetGValue(c) ((BYTE)(((WORD)(c))>>8))
#define GetBValue(c) ((BYTE)((c)>>16))
#define RGB(r,g,b) ((COLORREF)((BYTE)(r)|((BYTE)(g) << 8)|((BYTE)(b) << 16)))

#define BI_RGB 0

#ifndef FALSE
#define FALSE 0
#endif

#ifndef TRUE
#define TRUE 1
#endif

#ifndef max
#define max(a,b) ((a)>(b)?(a):(b))
#endif

#ifndef min
#define min(a,b) ((a)<(b)?(a):(b))
#endif

#define ZeroMemory RtlZeroMemory
#ifndef RtlZeroMemory
#define RtlZeroMemory(Destination, Length) \
  memset(Destination, 0, Length)
#endif

#define FillMemory RtlFillMemory
#ifndef RtlFillMemory
#define RtlFillMemory(Destination, Length, Fill) \
  memset(Destination, Fill, Length)
#endif

// Evil kludge for MS secure functions

// #define sprintf_s(b,s,...) sprintf(b,__VA_ARGS__)
// #define fopen_s(f,n,m) *f = fopen(n,m)

#define TRACE(...)

typedef unsigned char BYTE;
typedef unsigned int  BOOL;
typedef unsigned short WORD;
typedef unsigned long DWORD;
typedef void *PVOID,*LPVOID;
typedef DWORD COLORREF,*LPCOLORREF;
typedef BYTE *PBYTE,*LPBYTE;
typedef DWORD *DWORD_PTR;
typedef unsigned int UINT;
typedef long LONG;

typedef struct tagRGBQUAD
{
    BYTE    rgbBlue;
    BYTE    rgbGreen;
    BYTE    rgbRed;
    BYTE    rgbReserved;
} RGBQUAD,*LPRGBQUAD;

typedef struct tagPOINT {
	LONG x;
	LONG y;
} POINT,POINTL,*PPOINT,*LPPOINT,*PPOINTL,*LPPOINTL;

typedef struct tagSIZE {
	LONG cx;
	LONG cy;
} SIZE,SIZEL,*PSIZE,*LPSIZE,*PSIZEL,*LPSIZEL;

typedef SIZE CSize;

typedef struct tagBITMAPFILEHEADER {
	WORD	bfType;
	DWORD	bfSize;
	WORD	bfReserved1;
	WORD	bfReserved2;
	DWORD	bfOffBits;
} BITMAPFILEHEADER,*LPBITMAPFILEHEADER,*PBITMAPFILEHEADER;

typedef struct tagBITMAPINFOHEADER{
	DWORD	biSize;
	LONG	biWidth;
	LONG	biHeight;
	WORD	biPlanes;
	WORD	biBitCount;
	DWORD	biCompression;
	DWORD	biSizeImage;
	LONG	biXPelsPerMeter;
	LONG	biYPelsPerMeter;
	DWORD	biClrUsed;
	DWORD	biClrImportant;
} BITMAPINFOHEADER,*LPBITMAPINFOHEADER,*PBITMAPINFOHEADER;

typedef struct videohdr_tag {
	LPBYTE lpData;
	DWORD dwBufferLength;
	DWORD dwBytesUsed;
	DWORD dwTimeCaptured;
	DWORD dwUser;
	DWORD dwFlags;
	DWORD_PTR dwReserved[4];
} VIDEOHDR, *LPVIDEOHDR;

#endif

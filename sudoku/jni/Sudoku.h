
#pragma once

#include "stdafx.h"
#include "SudBitmap.h"
#include "org_billthefarmer_sudoku_Sudoku.h"

class Sudoku
{
public:
    BOOL enableProcessing;
    BOOL rectDetected;
    BOOL ocrValid;

    POINT rect[4];
    int puzzle[9][9];

    Bin strongestLine;
    DisplaySud display;

    Sudoku();
    ~Sudoku();

    void process(BYTE[], SIZE, DWORD);
};

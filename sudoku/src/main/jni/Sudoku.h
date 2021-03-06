////////////////////////////////////////////////////////////////////////////////
//
//  Sudoku - An Android Sudoku library.
//
//  Copyright (C) 2014	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

#pragma once

#include "stdafx.h"
#include "SudBitmap.h"
#include "org_billthefarmer_sudoku_Sudoku.h"

// Sudoku

class Sudoku
{
public:
    BOOL rectDetected;
    BOOL ocrValid;
    BOOL sudSolved;

    POINT rect[4];
    int puzzle[9][9];

    Bin strongestLine;
    DisplaySud display;

    Sudoku(void *);
    ~Sudoku();

    void process(BYTE[], SIZE, DWORD);
};

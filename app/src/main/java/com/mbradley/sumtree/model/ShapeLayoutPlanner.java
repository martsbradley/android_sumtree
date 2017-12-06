package com.mbradley.sumtree.model;

import com.mbradley.sumtree.layout.shape.Shape;
import com.mbradley.sumtree.model.key.ParseResult;


interface ShapeLayoutPlanner
{
    ParseResult getParseResult(Shape shape);
}

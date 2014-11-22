
#pragma version(1)
#pragma rs java_package_name(org.billthefarmer.camera)

rs_allocation in;
rs_allocation out;

int width;
int height;

uchar4 __attribute__((kernel)) rotate(uchar4 rgba, uint32_t x, uint32_t y)
{
    const uchar4 *element = rsGetElementAt(in, y, height - x - 1);
    return *element;
}

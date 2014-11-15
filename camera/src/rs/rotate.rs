
#pragma version(1)
#pragma rs java_package_name(org.billthefarmer.camera)

rs_allocation in;
rs_allocation out;

int width;
int height;

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData,
	  uint32_t x, uint32_t y)
{
    const uchar4 *element = rsGetElementAt(in, y, height - x - 1);
    *v_out = *element;
}

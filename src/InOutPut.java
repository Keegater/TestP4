public class InOutPut extends Port {

    public String name;
    public int row;
    public int col;
    public String type;
    public String[] values;
    public int curval = 0; //use this to set val = to "values[currval]"

    public InOutPut(String[] inOutArr){
        super();
        name = inOutArr[0];
        row = Integer.parseInt(inOutArr[1].split(" ")[0]);
        col = Integer.parseInt(inOutArr[1].split(" ")[1]);
        if ( name.charAt(0) == 'I' ){
            type = "Input";
        } else {
            type = "Output";
        }
        values = new String[inOutArr.length - 2];
        for (int i = 2; i < inOutArr.length; i++) {
            values[i-2] = inOutArr[i];
        }
    }

    public synchronized int read() {
        int returnval = Integer.parseInt(values[curval]);
        curval++;
        return returnval;
    }

    public synchronized void write(int value) {

    }




}

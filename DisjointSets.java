
public class DisjointSets {
    public int n;
    private int[] setArr;

        /***
         * Constructor (required to create disjoint sets)
         * @param numElts initially sets are singletons: {0}, {1}, . . . {numElts-1}
         */
        public DisjointSets(int numElts){
            n = numElts;
            int[] tempArr = new int[n];
            for(int i=0; i<n; i++){
                tempArr[i] = -1;
            }
            setArr = tempArr;
    }
 public int Find(int k) {
            // if `k` is not the root
            if (setArr[k] >= 0)
            {
                // path compression
                setArr[k] =  Find(setArr[k]);
                return setArr[k];
            }
            else{
                return k;
            }

        }
    

    /***
     * Take union of two sets.  Union by rank
     * @param a int in one set
     * @param b int in the other set
     */
    public void Union(int source, int dest)
    {
        // find the root of the sets in which elements `x` and `y` belongs
        int x = Find(source);
        int y = Find(dest);

        // if `x` and `y` are present in the same set
        if (x == y) {
            return;
        }
    }

}
    
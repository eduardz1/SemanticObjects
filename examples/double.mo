class <T> List (List<T> next, T content, List<T> previous)
   List<T> append(List<T> node)
      if this.next = null then
         this.next := node;
         node.previous := this;
      else
        this.next.append(node);
      end
      return this;
   end

   List<T> insert_after(List<T> node)
        node.next := this.next;
        node.previous := this;
        if this.next <> null then
            this.next.previous := node;
            this.next := node;
        else skip; end
        return this;
   end


   List<T> remove()
        if this.next <> null then
            this.next.previous := this.previous;
        else skip; end
        if this.previous <> null then
            this.previous.next := this.next;
        else skip; end
        this.next := null;
        this.previous := null;
        return this;
   end


   List<T> remove_unclean()
        if this.next <> null then
            this.next.previous := this.previous;
        else skip; end
        if this.previous <> null then
            this.previous.next := this.next;
        else skip; end
        this.next := null;
        return this;
   end
end


main
  List<Int> a := new List<Int>(null, 1, null);
  List<Int> b := new List<Int>(null, 2, null);
  List<Int> c := new List<Int>(null, 4, null);
  List<Int> d := new List<Int>(null, 3, null);
  a.append(b);
  a.append(c);
  b.insert_after(d);
  c.remove();
  breakpoint;
  print(a.next.next.content);
end

class <T, S> C(Int i, List<Int> list)
    Int getI() return this.i; end
    T id(T t) return t; end
    S idTwo(S s) return s; end
    Int sum(Int i1, Int i2)
        Int get := this.list.content;
        Int another := this.sum(get, i2);
        return i1+another;
    end
end

class <T> Wrapper(T content)

end

main
    skip;
end
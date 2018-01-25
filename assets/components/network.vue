<script>
export default {

  mounted() {
    this.$socket.on("cluster-state", state => {
      this.members = state.members;
    });
    this.$socket.emit("cluster-request-state");
  },

  destroyed() {
    this.$socket.off("cluster-state");
  },

  data() {
    return {
      address: "",
      members: [],
    }
  },
  methods: {
    joinNetwork() {
        this.$socket.emit("join-network", this.address);
    }
  }
};
</script>

<template>
    <div>
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
            <h1 class="h2">Network</h1>
            <div class="btn-toolbar mb-2 mb-md-0">
                <div class="input-group mr-2">
                    <input type="text" class="form-control" placeholder="ip:port" v-model="address">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button" @click="joinNetwork()">Join</button>
                    </div>
                </div>
            </div>
        </div>

        <table class="table table-hover">
            <thead>
            <tr>
                <th>Address</th>
                <th>Roles</th>
                <th>State</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="member in members">
                <td>{{member.name}}</td>
                <td>
                    <span class="badge badge-primary" v-for="role in member.roles">{{role}}</span>
                </td>
                <td>{{member.state}}</td>
            </tr>

            </tbody>
        </table>
    </div>
</template>